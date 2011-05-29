package frontend.client;

import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.MultiUploader;
import gwtupload.client.IUploader;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import middle.ImageType;

import utils.ConcurrentVar;
import utils.EntityExistsNotException;
import utils.HomeUtils;
import utils.InvalidDataException;
import frontend.client.ImagesGallery.FormStateMachine;
import frontend.client.ImagesGallery.FormStateMachine.OperationTypeMachine.OperationType;
import frontend.shared.ImageFEObject;
import frontend.shared.ImageGalleryOptionsFEObject;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import static gwtupload.client.IUploadStatus.Status;
import gwtupload.client.*;
/**
 * This class could have had simpler structure, but in such case it would have huge amount of
 * dangerous sections, high probability of errors and awful scalability.
 * Current solution has an additional dimension for consumption of complexity, it is more state-consistent, much easier to modify, etc.
 */
public class ImagesGallery implements EntryPoint {
    private static int CFG__TABLE_WIDTH = 500; 
    private static int CFG__TABLE_HEIGHT = 350;
    private static int CFG__IMG_WIDTH = 400;
    private static int CFG__FORMFIELDS_WIDTH = 300;
    private static int CFG__THUMBNAIL_SIZE = 250;
    
    /**
     * The message displayed to the user when the server cannot be reached or
     * returns an error.
     */
    private static final String SERVER_ERROR = "An error occurred while "
            + "attempting to contact the server. Please check your network "
            + "connection and try again.";

    /**
     * Create a remote service proxy to talk to the server-side Gallery service.
     */
    
    private static final String ERR__NoImages = "No images in DB";
    private static final String ERR__NoErrors = "No errors";
    private static final String ERR__FormButtonPressedForNullForm = "Illegal state: form button pressed, while there's no active form!";
    
    // GWT 2.2.0 core won't support String.format(...) 
    // private static final String MSG__RemovalTargetImgId = "Image with ID: %1$d";
    // private static final String MSG__CRUD_IMGOP_SUCCESS = "Successfull operation \"%1$s\" with image ID: %2$d!";
    // private static final String MSG__CRUD_OP_SUCCESS = "Successfull operation \"%1$s\"!";
    public static String MSG__RemovalTargetImgId(Integer imgId) { return "Target image ID: " + imgId; }
    public static String MSG__CRUD_IMGOP_SUCCESS(String op, Integer imgId) { return "Successfull operation \"" + op + "\" with image ID: " + imgId + "!"; }
    public static String MSG__CRUD_OP_SUCCESS(String op) { return "Successfull operation \"" + op + "\"!"; }
    
    public abstract static class FormStateMachine {
    
        private enum FormState {
            UI, LOADING, STATUS;
            
            public Panel getFormPanel(FormStateMachine sm) {
                switch(this) {
                    case UI:      return sm.panFormUI;
                    case LOADING: return sm.panFormLoading;
                    case STATUS:  return sm.panFormAnswerStatus;
                }
                throw new UnsupportedOperationException(this.name());            
            }
            
        }
        
        public static abstract class OperationTypeMachine {
            public enum OperationType {
                ADD_IMG, REMOVE_IMG, OPTIONS;
                
                public Panel getFormFieldsPanel(OperationTypeMachine otm) {
                    switch(this) {
                        case ADD_IMG:    return otm.panFormFieldsAdd;
                        case REMOVE_IMG: return otm.panFormFieldsRemove;
                        case OPTIONS:    return otm.panFormFieldsOptions;
                    }
                    throw new UnsupportedOperationException(this.name());            
                }
                public boolean commitWouldRequireTableRefresh() {
                    switch(this) {
                        case ADD_IMG:    
                        case REMOVE_IMG: return true;
                        case OPTIONS:    return false;
                    }
                    throw new UnsupportedOperationException(this.name());
                }
                public String toHumanReadable() {
                    switch(this) {
                        case ADD_IMG:    return "Image addition";
                        case REMOVE_IMG: return "Image removal";
                        case OPTIONS:    return "Options change";
                    }
                    throw new UnsupportedOperationException(this.name());
                }
            }
            
            private volatile OperationType currentOperation = null;
            
            private final CommunicationsWithServer comWithServer;
            private final ErrorsControl ctrlErrors;
            private final FormStateMachine machFormState;
            
            private final Panel panFormFieldsAdd     = new VerticalPanel();
            private final Panel panFormFieldsRemove  = new VerticalPanel();
            private final Panel panFormFieldsOptions = new VerticalPanel();
            
            // form add image
            private final Label   lbImageName = new Label("Image title: ");
            private final TextBox tbImageName = new TextBox();
            private final Panel  panImageName = new HorizontalPanel();
            
            private final Label   lbImageURL  = new Label("Image URL: ");
            private final TextBox tbImageURL  = new TextBox();
            private final Panel  panImageURL  = new HorizontalPanel();
            
            private final Label   lbImageSize = new Label("Image size (bytes): ");
            private final Label   tbImageSize = new Label(); // content set dynamically
            private final Panel  panImageSize = new HorizontalPanel();
            
            private final Label   lbImageType = new Label("Image type: ");
            private final Label   tbImageType = new Label(); // content set dynamically
            private final Panel  panImageType = new HorizontalPanel();
            
            private final Panel  panThumbnail = new SimplePanel();
            private final SingleUploader suplImageUploader = new SingleUploader();
            
            // form remove image
            private final Label lbTargetImageInfo = new Label(); // content set dynamically
            private final Label lbDeleteQuestion  = new Label("Are you sure to delete this image?");
            private Integer imgPK;
            
            // form options
            private final Label   lbImgSizeCnstr = new Label("Image max size (in bytes): ");
            private final TextBox tbImgSizeCnstr = new TextBox();
            private final Panel  panImgSizeCnstr = new HorizontalPanel();
            
            private final Label    lbImgTypeCnstr = new Label("Allowed (for upload) images types: ");
            private final ListBox libImgTypeCnstr = new ListBox(true);
            private final Panel   panImgTypeCnstr = new HorizontalPanel();
            
            protected abstract void onFormCommit(OperationType committed_ot);
            
            public OperationTypeMachine(CommunicationsWithServer _comWithServer, ErrorsControl _ctrlErrors, FormStateMachine _machFormState) {
                super();
                
                ctrlErrors = _ctrlErrors;
                comWithServer = _comWithServer;
                machFormState = _machFormState; 
                
                final OperationTypeMachine OTS_THIS = this;
                
                // ==================================
                // LAYOUT
                
                machFormState.getPanFormFields().add(panFormFieldsAdd);
                machFormState.getPanFormFields().add(panFormFieldsRemove);
                machFormState.getPanFormFields().add(panFormFieldsOptions);
                
                panFormFieldsAdd.add(panImageName);
                panFormFieldsAdd.add(panImageURL);
                panFormFieldsAdd.add(panImageSize);
                panFormFieldsAdd.add(panImageType);
                panFormFieldsAdd.add(suplImageUploader);
                panFormFieldsAdd.add(panThumbnail);
                // panFormFieldsAdd.add(suplImageUploader.getStatusWidget().getWidget());
                
                panFormFieldsRemove.add(lbTargetImageInfo);
                panFormFieldsRemove.add(lbDeleteQuestion);
                
                panFormFieldsOptions.add(panImgSizeCnstr);
                panFormFieldsOptions.add(panImgTypeCnstr);
                
                panImageName.add(lbImageName);
                panImageName.add(tbImageName);
                
                panImageURL.add(lbImageURL);
                panImageURL.add(tbImageURL);
                
                panImgSizeCnstr.add(lbImgSizeCnstr);
                panImgSizeCnstr.add(tbImgSizeCnstr);
                
                panImgTypeCnstr.add( lbImgTypeCnstr);
                panImgTypeCnstr.add(libImgTypeCnstr);
                
                panImageSize.add(lbImageSize);
                panImageSize.add(tbImageSize);
                
                panImageType.add(lbImageType);
                panImageType.add(tbImageType);
                
                // ==================================
                // CONTROLS STYLES AND INITIAL CONTENTS
                
                panFormFieldsAdd.setVisible(false);
                panFormFieldsRemove.setVisible(false);
                panFormFieldsOptions.setVisible(false);
                
                for(ImageType it : ImageType.values())
                    libImgTypeCnstr.addItem(it.getTitle());
                libImgTypeCnstr.setVisibleItemCount(6);
                
                suplImageUploader.avoidRepeatFiles(false);
                
                this.comWithServer.getOptionsService().getCurrentOptions(new AsyncCallback<ImageGalleryOptionsFEObject>() {
                    @Override public void onFailure(Throwable caught) { 
                        OTS_THIS.ctrlErrors.addError(caught);
                    }
                    @Override
                    public void onSuccess(ImageGalleryOptionsFEObject result) {
                        List<String> allowedTypesExts = new LinkedList<String>();
                        for(ImageType it : result.getImgAllowedTypes()) {
                            allowedTypesExts.addAll(it.getAssociatedExtensions());
                        }
                        OTS_THIS.suplImageUploader.setValidExtensions(allowedTypesExts.toArray(new String[allowedTypesExts.size()]));
                    }
                });
                
                panThumbnail.setStylePrimaryName("thumbnail");
                
                // ==================================
                // CONTROLS BEHAVIOUR
                
                // Load the image in the document and in the case of success attach it to the viewer
                IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
                    public void onFinish(IUploader uploader) {
                        if (uploader.getStatus() == Status.SUCCESS) {
                            
                            // The server sends useful information to the client by default
                            UploadedInfo info = uploader.getServerInfo();
                            
                            tbImageName.setFocus(true);
                            tbImageURL.setText(Window.Location.getProtocol() + "//" + Window.Location.getHost() + "/" + uploader.fileUrl());
                            tbImageSize.setText(String.valueOf(info.size));
                            tbImageType.setText(info.ctype);
                            
                            new PreloadedImage(uploader.fileUrl(), new OnLoadPreloadedImageHandler() {
                                    public void onLoad(PreloadedImage image) {
                                        panThumbnail.clear();
                                        image.setWidth(CFG__THUMBNAIL_SIZE + "px");
                                        panThumbnail.add(image);
                                    }
                                }
                            );
                            
                            System.out.println("File name " + info.name);
                            System.out.println("File content-type " + info.ctype);
                            System.out.println("File size " + info.size);

                            // You can send any customized message and parse it 
                            System.out.println("Server message " + info.message);
                        }
                    }
                };
                
                // Add a finish handler which will load the image once the upload finishes
                suplImageUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
            }
            
            public void setVisible(boolean visibility) {
                OperationType curOp = this.currentOperation;
                
                if(curOp == null) return;
                
                switch(curOp) {
                    case ADD_IMG:    
                        this.panFormFieldsAdd.setVisible(visibility);
                        break;
                    case REMOVE_IMG: 
                        this.panFormFieldsRemove.setVisible(visibility);
                        break;
                    case OPTIONS:    
                        this.panFormFieldsOptions.setVisible(visibility);
                        break;
                    default: throw new UnsupportedOperationException(curOp.name()); 
                }
                            
            }
            
            public void initState(OperationType new_ot, ImageFEObject perhaps_operationTarget) {
                if(new_ot == null) {
                    assert perhaps_operationTarget == null;
                    
                    this.setVisible(false);
                }
                
                switch(new_ot) {
                    case ADD_IMG:    
                        assert perhaps_operationTarget == null;
                        
                        this.tbImageName.setText("");
                        this.tbImageURL.setText("");
                        this.tbImageSize.setText("");
                        this.tbImageType.setText("");
                        panThumbnail.clear();
                        
                        break;
                    case REMOVE_IMG: 
                        assert perhaps_operationTarget != null;
                        imgPK = perhaps_operationTarget.getId();
                        lbTargetImageInfo.setText(MSG__RemovalTargetImgId(imgPK));
                        break;
                    case OPTIONS: 
                        assert perhaps_operationTarget == null;
                        
                        this.machFormState.toggleLoading();
                        final OperationTypeMachine super_this = this;
                        
                        this.comWithServer.getOptionsService().getCurrentOptions(new AsyncCallback<ImageGalleryOptionsFEObject>() {
                            @Override public void onFailure(Throwable caught) { 
                                if(OperationType.OPTIONS.equals(super_this.currentOperation) && super_this.machFormState.isLoading()) {
                                    super_this.ctrlErrors.addError(caught);
                                    super_this.setVisible(false);
                                    super_this.machFormState.cancel();
                                }
                            }
                            @Override
                            public void onSuccess(ImageGalleryOptionsFEObject result) {
                                if(OperationType.OPTIONS.equals(super_this.currentOperation) && super_this.machFormState.isLoading()) {
                                    super_this.tbImgSizeCnstr.setText(String.valueOf(result.getImgMaxSize_bytes()));
                                    
                                    int itemsCount = super_this.libImgTypeCnstr.getItemCount();
                                    
                                    for(int i = 0; i < itemsCount; i++) {
                                        ImageType lbImageType = ImageType.fromTitle(super_this.libImgTypeCnstr.getItemText(i)); 
                                        boolean selected = result.getImgAllowedTypes().contains(lbImageType); 
                                        super_this.libImgTypeCnstr.setItemSelected(i, selected);
                                    }
                                    
                                    super_this.machFormState.toggleUI();                        
                                }
                            }
                        });                            
                        break;
                    default: throw new UnsupportedOperationException(new_ot.name()); 
                }
                this.setVisible(false);
                this.currentOperation = new_ot;
                this.setVisible(true);
            }
            
            public ImageFEObject getImgFromForm_forCreation() throws InvalidDataException {
                ImageFEObject imgToSave = new ImageFEObject();
                
                imgToSave.setId(-1); // will be autognerated by the back-end
                imgToSave.setTitle(tbImageName.getText());
                imgToSave.setUrl(tbImageURL.getText());
                if(tbImageSize.getText() != "")
                     imgToSave.setSize(Long.parseLong(tbImageSize.getText()));
                else imgToSave.setSize(0);
                if(tbImageType.getText() != "")
                     imgToSave.setType(ImageType.fromMime(tbImageType.getText()));
                else imgToSave.setType(ImageType.NET);
                
                return imgToSave;
            }
            
            public ImageGalleryOptionsFEObject getOptionsFromForm_forUpdate() throws InvalidDataException {
                ImageGalleryOptionsFEObject optsToSave = new ImageGalleryOptionsFEObject();
                
                try {
                    optsToSave.setImgMaxSize_bytes(Long.parseLong(tbImgSizeCnstr.getText()));
                } catch (NumberFormatException e) {
                    throw new InvalidDataException("Iamge size constraint field should contain a number!", e);
                }
                
                Set<ImageType> newAllowedTypes = new HashSet<ImageType>();
                Integer itemsCount = this.libImgTypeCnstr.getItemCount();
                for(int i = 0; i < itemsCount; i++) { 
                    if(this.libImgTypeCnstr.isItemSelected(i))
                        newAllowedTypes.add(ImageType.fromTitle(libImgTypeCnstr.getItemText(i))); // TODO: it's actually safe to get ImageType by index from enum
                }
                optsToSave.setImgAllowedTypes(newAllowedTypes);
                
                return optsToSave;
            }
            
            public void performOperation(final OperationType currentOp) {
                assert currentOp != null;
                
                final OperationTypeMachine super_this = this;
                
                switch(currentOp) {
                    case ADD_IMG:
                        ImageFEObject imgToSave = null;
                        try {
                            imgToSave = getImgFromForm_forCreation();
                        } catch (InvalidDataException e) {
                            e.printStackTrace();
                            super_this.ctrlErrors.addError(e); // TODO: form should have dedicated error-output
                            super_this.machFormState.toggleUI();
                        }
                        
                        if(imgToSave != null)
                            this.comWithServer.getGalleryService().create(imgToSave, new AsyncCallback<Integer>(){
                                @Override public void onFailure(Throwable caught) { 
                                    if(currentOp.equals(super_this.currentOperation) && super_this.machFormState.isLoading()) {
                                        super_this.ctrlErrors.addError(caught); // TODO: form should have dedicated error-output
                                        super_this.machFormState.toggleUI();
                                    }                                
                                }
                                @Override
                                public void onSuccess(Integer result) {
                                    if(currentOp.equals(super_this.currentOperation) && super_this.machFormState.isLoading()) {
                                        super_this.onFormCommit(currentOp);
                                        super_this.machFormState.toggleStatus(MSG__CRUD_IMGOP_SUCCESS(currentOp.toHumanReadable(), result));
                                    }
                                }
                            });
                        break;
                    case REMOVE_IMG:
                        final Integer tagetImgId = super_this.imgPK;
                        
                        this.comWithServer.getGalleryService().deleteByPK(tagetImgId, new AsyncCallback<ImageFEObject>(){
                            @Override public void onFailure(Throwable caught) { 
                                if(currentOp.equals(super_this.currentOperation) && super_this.machFormState.isLoading()) {
                                    super_this.ctrlErrors.addError(caught); // TODO: form should have dedicated error-output
                                    super_this.machFormState.toggleUI();
                                }                                
                            }
                            @Override
                            public void onSuccess(ImageFEObject result) {
                                if(currentOp.equals(super_this.currentOperation) && super_this.machFormState.isLoading()) {
                                    super_this.onFormCommit(currentOp);
                                    super_this.machFormState.toggleStatus(MSG__CRUD_IMGOP_SUCCESS(currentOp.toHumanReadable(), result.getId()));
                                }
                            }
                        });
                        break;
                    case OPTIONS:
                        ImageGalleryOptionsFEObject optsToSave = null;
                        try {
                            optsToSave = getOptionsFromForm_forUpdate();
                        } catch (InvalidDataException e) {
                            e.printStackTrace();
                            super_this.ctrlErrors.addError(e); // TODO: form should have dedicated error-output
                            super_this.machFormState.toggleUI();
                        }
                        
                        if(optsToSave != null) {
                            final ImageGalleryOptionsFEObject final_optsToSave = optsToSave;
                            
                            this.comWithServer.getOptionsService().updateOptions(optsToSave, new AsyncCallback<Void>(){
                                @Override public void onFailure(Throwable caught) { 
                                    if(currentOp.equals(super_this.currentOperation) && super_this.machFormState.isLoading()) {
                                        super_this.ctrlErrors.addError(caught); // TODO: form should have dedicated error-output
                                        super_this.machFormState.toggleUI();
                                    }                                
                                }
                                @Override
                                public void onSuccess(Void result) {
                                    if(currentOp.equals(super_this.currentOperation) && super_this.machFormState.isLoading()) {
                                        super_this.onFormCommit(currentOp);
                                        super_this.machFormState.toggleStatus(MSG__CRUD_OP_SUCCESS(currentOp.toHumanReadable()));

                                        List<String> allowedTypesExts = new LinkedList<String>();
                                        for(ImageType it : final_optsToSave.getImgAllowedTypes()) {
                                            allowedTypesExts.addAll(it.getAssociatedExtensions());
                                        }
                                        super_this.suplImageUploader.setValidExtensions(allowedTypesExts.toArray(new String[allowedTypesExts.size()]));
                                    }
                                }
                            });
                        }
                        
                        break;
                }
            }
        }
        
        private volatile FormState state;
        
        private final CommunicationsWithServer comWithServer;
        private final ErrorsControl ctrlErrors;
        
        private final Panel panForm             = new HorizontalPanel();
        private final Panel panFormUI           = new HorizontalPanel();
        private final Panel panFormLoading      = new VerticalPanel();
        private final Panel panFormAnswerStatus = new VerticalPanel();
        
        private final Panel panFormFieldsC = new VerticalPanel(); // content set dynamically
        private final Panel panFormFields  = new FormPanel(); // content set dynamically
        private final Panel panSplitBetweenFieldsAndButtons = new SimplePanel();
        private final Panel panFormButtons = new VerticalPanel();
        
        private final Label lbOperationFeedback = new Label(); // content set dynamically
        private final Image imgLoading = new Image("/loading.gif");
        
        private final Button btnFormCommit = new Button("Commit"); 
        private final Button btnFormCancel = new Button("Cancel");
        // TODO: a safer approach would be to have separate buttons for each form
        // despite they would be identical, the control flow would be more secured
        
        private  final OperationTypeMachine machOperationType;         
        protected abstract void onFormCommit(OperationType committed_ot);
        
        public FormStateMachine(CommunicationsWithServer _comWithServer, ErrorsControl _ctrlErrors) {
            super();
            
            comWithServer = _comWithServer;
            ctrlErrors = _ctrlErrors;
            
            final FormStateMachine FSM_SUPER = this;
            
            machOperationType = new OperationTypeMachine(comWithServer, _ctrlErrors, this){
                @Override
                protected void onFormCommit(OperationType committed_ot) {
                    FSM_SUPER.onFormCommit(committed_ot);                    
                }                
            };
            
            // ==================================
            // LAYOUT
            
            panForm.add(panFormUI);
            panForm.add(panFormLoading);
            panForm.add(panFormAnswerStatus);

            panFormUI.add(panFormFields);
            panFormUI.add(panSplitBetweenFieldsAndButtons);
            panFormUI.add(panFormButtons);
            
            panFormFields.add(panFormFieldsC);

            panFormLoading.add(imgLoading);
            panFormAnswerStatus.add(lbOperationFeedback);
            
            panFormButtons.add(btnFormCommit);
            panFormButtons.add(btnFormCancel);
            
            // ==================================
            // CONTROLS STYLES AND INITIAL CONTENTS
            
            panSplitBetweenFieldsAndButtons.setWidth("10px");    
            panSplitBetweenFieldsAndButtons.setHeight("1px");
            
            panFormFields.setWidth(CFG__FORMFIELDS_WIDTH + "px");
            panFormFieldsC.setWidth(CFG__FORMFIELDS_WIDTH + "px");
            
            panFormUI.setVisible(false);
            panFormLoading.setVisible(false);
            panFormAnswerStatus.setVisible(false);
            
            lbOperationFeedback.setStyleName("success");
            
            // ==================================
            // CONTROLS BEHAVIOUR
            btnFormCommit.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    OperationType currentOp = FSM_SUPER.machOperationType.currentOperation;
                    
                    if(currentOp == null) {
                        FSM_SUPER.ctrlErrors.addError(ERR__FormButtonPressedForNullForm);
                        FSM_SUPER.cancel();
                        return;
                    }
                    
                    FSM_SUPER.toggleLoading();
                    FSM_SUPER.machOperationType.performOperation(currentOp);
                }
            });
            
            btnFormCancel.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    FSM_SUPER.cancel();
                }
            });

        }

        public void cancel() {
            FormState oldState = state;
            if(oldState != null) oldState.getFormPanel(this).setVisible(false);
            state = null;
        }
        
        public void forward() {
            FormState oldState = state;
            FormState newState = null; 
            if(oldState == null) {
                newState = FormState.UI;
            } else {
                oldState.getFormPanel(this).setVisible(false);
                switch(oldState) {
                    case UI: newState = FormState.LOADING; break; 
                    case LOADING: newState = FormState.STATUS; break;
                    case STATUS: 
                    default: throw new UnsupportedOperationException(oldState.name()); 
                }
            }
            state = newState;
            newState.getFormPanel(this).setVisible(true);
        }
        
        public void back() {
            FormState oldState = state;
            FormState newState = null; 
            if(oldState == null) {
                throw new UnsupportedOperationException(oldState.name());                
            } else {
                oldState.getFormPanel(this).setVisible(false);
                switch(oldState) {
                    case LOADING: newState = FormState.UI; break;
                    case UI: 
                    case STATUS: 
                    default: throw new UnsupportedOperationException(oldState.name()); 
                }
            }
            state = newState;
            newState.getFormPanel(this).setVisible(true);
        }
        
        public void toggleLoading() {
            FormState oldState = state;
            FormState newState = FormState.LOADING; 
            if(oldState != null) 
                oldState.getFormPanel(this).setVisible(false);
            state = newState;
            newState.getFormPanel(this).setVisible(true);
        }
        
        public void toggleUI() {
            FormState oldState = state;
            FormState newState = FormState.UI; 
            if(oldState != null) 
                oldState.getFormPanel(this).setVisible(false);
            state = newState;
            newState.getFormPanel(this).setVisible(true);
        }
        
        protected void toggleStatus(String msg) {
            FormState oldState = state;
            FormState newState = FormState.STATUS; 
            if(oldState != null) 
                oldState.getFormPanel(this).setVisible(false);
            assert FormState.LOADING.equals(oldState);
            
            lbOperationFeedback.setText(msg);
            state = newState;
            newState.getFormPanel(this).setVisible(true);
            
        }
        
        public boolean isOperational() {
            FormState curState = state;
            if(curState == null) return false;
            switch(curState) {
                case LOADING: 
                case UI:     return true;
                case STATUS: return false;
                default: throw new UnsupportedOperationException(curState.name()); 
            }
        }
        
        public boolean isLoading() {
            return state == FormState.LOADING;
        }
        
        public void initOp(OperationTypeMachine.OperationType op, ImageFEObject img) {
            this.cancel();
            if(op == null) { 
                assert img == null;
                return;
            }
            this.machOperationType.initState(op, img);
            if(op != OperationTypeMachine.OperationType.OPTIONS)
                this.toggleUI();     
        }
        
        public Panel getRootFormPanel() {
            return panForm;
        }

        public Panel getPanFormFields() {
            return panFormFieldsC;
        }
    }
    
    // CRUDO = Create/Remove/Update/Delete + Options 
    // a common abbreviation for usual control block
    // , that usually includes 3 elements: a table, a set of CRUDO buttons and a form
    public class CrudoControlBox {
        private final Button btnAdd        = new Button("Add image...");
        private final Button btnRemove     = new Button("Remove image...");
        private final Button btnOptions    = new Button("Options");
        private final Button btnRefresh    = new Button("Refresh table");
        
        final Panel panTable = new VerticalPanel();
        
        private final CellTable<ImageFEObject> tabImages = new CellTable<ImageFEObject>(); // content set dynamically
        private final SimplePager simplePager = new SimplePager(); 
        private final SingleSelectionModel<ImageFEObject> tabImagesSelectionModel = new SingleSelectionModel<ImageFEObject>();
        private final Panel panSplitBetweenTableAndImage = new SimplePanel();
        private final Panel panImage = new SimplePanel(); // content set dynamically
        
        private final CommunicationsWithServer comWithServer;
        private final ErrorsControl ctrlErrors;        
        private final FormStateMachine formStateMachine;
        
        public CrudoControlBox(CommunicationsWithServer comWithServer, ErrorsControl ctrlErrors) {
            super();
            this.comWithServer = comWithServer;
            this.ctrlErrors = ctrlErrors;
            
            final CrudoControlBox CRUDO_THIS = this;
            
            formStateMachine = new FormStateMachine(comWithServer, ctrlErrors) {
                @Override
                protected void onFormCommit(OperationType committed_ot) {
                    assert committed_ot != null;
                    if(committed_ot.commitWouldRequireTableRefresh())
                        CRUDO_THIS.refreshTable();
                }                
            };
            
            panTable.add(tabImages);
            panTable.add(simplePager);
            
            // ==================================
            // CONTROLS STYLES AND INITIAL CONTENTS
            panTable.setWidth(CFG__TABLE_WIDTH + "px");
            panTable.setHeight(CFG__TABLE_HEIGHT + "px");
            
            panSplitBetweenTableAndImage.setWidth("10px");    
            panSplitBetweenTableAndImage.setHeight("1px");
            
            // table
            tabImages.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
            
            TextColumn<ImageFEObject> colImageId = new TextColumn<ImageFEObject>() {
                @Override public String getValue(ImageFEObject object) { return String.valueOf(object.getId()); }
            };
            TextColumn<ImageFEObject> colImageTitle = new TextColumn<ImageFEObject>() {
                @Override public String getValue(ImageFEObject object) { return object.getTitle(); }
            };
            TextColumn<ImageFEObject> colImageURL = new TextColumn<ImageFEObject>() {
                @Override public String getValue(ImageFEObject object) { return object.getUrl(); }
            };
            tabImages.addColumn(colImageId, "Id");
            tabImages.addColumn(colImageTitle, "Title");
            tabImages.addColumn(colImageURL, "Url");
            
            simplePager.setDisplay(tabImages);
            simplePager.setPageSize(5); 
            final ListDataProvider<ImageFEObject> dataProvider = new ListDataProvider<ImageFEObject>();
            dataProvider.addDataDisplay(tabImages);
            
            // ==================================
            // CONTROLS BEHAVIOUR
            
            // Add a selection model to handle user selection.
            tabImages.setSelectionModel(tabImagesSelectionModel);
            tabImagesSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
                public void onSelectionChange(SelectionChangeEvent event) {
                    ImageFEObject selected = tabImagesSelectionModel.getSelectedObject();
                    if (selected != null) {
                        CRUDO_THIS.showImage(selected.getUrl()); 
                        // sharing single image between different URLs shows strange blinking in firefox
                    }
                }
            });
            
            btnRefresh.addClickHandler(new ClickHandler(){
                public void onClick(ClickEvent event) {
                    // TODO add loading animation
                    
                    CRUDO_THIS.comWithServer.getGalleryService().findAllSorted(new AsyncCallback<List<ImageFEObject>>() {
                        public void onFailure(Throwable caught) {
                            CRUDO_THIS.ctrlErrors.addError(caught);
                        }

                        public void onSuccess(List<ImageFEObject> result) {
                            tabImages.setRowData(0, result);
                            tabImages.setRowCount(result.size(), true);
                            tabImages.redraw();
                            
                            dataProvider.setList(result);                            
                        }
                    });
                }
            });

            btnAdd.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    CRUDO_THIS.formStateMachine.initOp(OperationType.ADD_IMG, null);
                }
            });
            
            btnRemove.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    CRUDO_THIS.formStateMachine.initOp(OperationType.REMOVE_IMG, tabImagesSelectionModel.getSelectedObject());
                }
            });
            
            btnOptions.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    CRUDO_THIS.formStateMachine.initOp(OperationType.OPTIONS, null);
                }
            });
        }
        
        public void showImage(String url) {
            Image img = new Image(url);
            img.setWidth(CFG__IMG_WIDTH + "px");
            panImage.clear();
            panImage.add(img);            
        }
        
        public void addCrudoButtonsToPanel(Panel pan) {
            pan.add(btnAdd);
            pan.add(btnRemove);
            pan.add(btnOptions);
            pan.add(btnRefresh);
        }
        
        public void addFormToPanel(Panel pan) {
            pan.add(formStateMachine.getRootFormPanel());
        }

        public void addViewerToPanel(Panel panRoot) {
            panRoot.add(panSplitBetweenTableAndImage);
            panRoot.add(panImage);            
        }

        public void addTableToPanel(Panel panRoot) {
            panRoot.add(this.panTable);
        }
        
        public void refreshTable() {
            btnRefresh.click();
        }
    }
    
    // ==================================
    // COMMUNICATION WITH SERVER
    public static class CommunicationsWithServer {
        private final ImagesGalleryServiceAsync galleryService = GWT.create(ImagesGalleryService.class);
        private final ImageGalleryOptionsServiceAsync optionsService = GWT.create(ImageGalleryOptionsService.class);
        
        public ImagesGalleryServiceAsync getGalleryService() {
            return galleryService;
        }
        public ImageGalleryOptionsServiceAsync getOptionsService() {
            return optionsService;
        }        
    }
    
    private final CommunicationsWithServer comWithServer = new CommunicationsWithServer();
    
    // ==================================
    // CONTROLS     
    
    public class ErrorsControl { 
        final HTML lbErrors = new HTML(); // content set dynamically
        
        public ErrorsControl() {
            super();
            lbErrors.setStylePrimaryName("error");
        }
        public void addError(String str) { lbErrors.setHTML(str + "<br>" + lbErrors.getText()); }
        public void addError(Throwable caught) { addError(caught.getMessage() + "(" + caught.getClass().getName() + ")"); }
        public void clearErrors() { lbErrors.setText(""); }
        public HTML getLbErrors() {
            return lbErrors;
        }
        
    }
    
    final ErrorsControl ctrlErrors = new ErrorsControl();  
    
    final Panel panRootWithErrors = new VerticalPanel();
    final Panel panRoot     = new HorizontalPanel();
    final Panel panCrudoComplect 
                            = new VerticalPanel();
    final Panel panTable    = new SimplePanel();
    final Panel panControls = new VerticalPanel();
    final Panel panCrudoButtons = new HorizontalPanel();
    
    final CrudoControlBox ctrlCrudo = new CrudoControlBox(comWithServer, ctrlErrors); 
    
    public void onModuleLoad() {

        // ==================================
        // LAYOUT
        
        // panRoot gets added to the RootPanel after all the setup is done
        
        panRootWithErrors.add(ctrlErrors.getLbErrors());
        panRootWithErrors.add(panRoot);
        
        panRoot.add(panCrudoComplect);
        ctrlCrudo.addViewerToPanel(panRoot);        
                
        panCrudoComplect.add(panTable);
        panCrudoComplect.add(panControls);
        
        ctrlCrudo.addTableToPanel(panTable);
        
        panControls.add(panCrudoButtons);
        
        ctrlCrudo.addCrudoButtonsToPanel(panCrudoButtons);
        ctrlCrudo.addFormToPanel(panControls);
        
        // ==================================
        // FINISH
        RootPanel.get("mainPanelContainer").add(panRootWithErrors);
        ctrlCrudo.refreshTable();        
    }
}
