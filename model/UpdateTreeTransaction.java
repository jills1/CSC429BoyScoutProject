package model;
import javafx.scene.Scene;
import java.util.Properties;
import exception.InvalidPrimaryKeyException;
import userinterface.View;
import userinterface.ViewFactory;
public class UpdateTreeTransaction extends Transaction {
    private String transactionErrorMessage = "";
    private String accountUpdateStatusMessage = "";
    protected  Tree myTree;
    public UpdateTreeTransaction() {
        super();
    }
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelDeposit", "CancelTransaction");
        dependencies.setProperty("OK", "CancelTransaction");
        dependencies.setProperty("RegisterTreeInfo", "TransactionError");
        dependencies.setProperty("UpdateTreeFormView", "TransactionError");
        myRegistry.setDependencies(dependencies);
    }
    public void processTransaction(Properties props) {
        try {
            String barcode= props.getProperty("barcode");
            myTree= new Tree(barcode);
            String treeType = (String) myTree.getState("treeType");
            props.setProperty("treeType", treeType);
            String treeStatus = (String) myTree.getState("status");
            props.setProperty("status", treeStatus);
            String treeNotes = (String) myTree.getState("notes");
            props.setProperty("notes", treeNotes);
            String treeDateStatusUpdate = (String) myTree.getState("dateStatusUpdate");
            props.setProperty("dateStatusUpdate", treeDateStatusUpdate);
            createAndShowUpdateTreeFormView();
        } catch(InvalidPrimaryKeyException e){
            transactionErrorMessage="Error cannot do this 2.";
        }
    }
    public Object getState(String key) {
        switch (key) {
            case "TransactionError":
                return transactionErrorMessage;
            case "UpdateStatusMessage":
                return accountUpdateStatusMessage;
            case "barcode":
                if (myTree != null) {
                    return myTree.getState("barcode");
                } else {
                    return "Undefined";
                }
            case "treeType":
                if (myTree != null) {
                    String treeTypeId = (String) myTree.getState("treeType");
                    TreeType tt = new TreeType();
                    try {
                        tt.populateWithId(treeTypeId);
                        return tt.getState("typeDescription");
                    } catch (InvalidPrimaryKeyException excep) {
                        return "";
                    }
                } else {
                    return "Undefined";
                }
            case "notes":
                if (myTree != null) {
                    return myTree.getState("notes");
                } else {
                    return "Undefined";
                }
            case "status":
                if (myTree != null) {
                    return myTree.getState("status");
                } else {
                    return "Undefined";
                }
            case "dateStatusUpdate":
                if (myTree != null) {
                    return myTree.getState("dateStatusUpdate");
                } else {
                    return "Undefined";
                }
        }
        return null;
    }
    public void stateChangeRequest(String key, Object value) {
        if (key.equals("DoYourJob")) {
            doYourJob();
        } else  if (key.equals("UpdateTreeFormView")) {
            processTransaction((Properties)value);
        }
        else  if (key.equals("RegisterTreeInfo")) {
            processUpdateTransaction((Properties)value);
        }
        myRegistry.updateSubscribers(key, this);
    }

    public void processUpdateTransaction(Properties props) {
        myTree = new Tree(props);
        myTree.update();
        transactionErrorMessage = "Tree Updated Successfully";
    }
    protected Scene createView() {
        Scene currentScene = myViews.get("UpdateTreeView");
        if (currentScene == null) {
            View newView = ViewFactory.createView("UpdateTreeView", this);
            assert newView != null;
            currentScene = new Scene(newView);
            myViews.put("UpdateTreeView", currentScene);
            return currentScene;
        } else {
            return currentScene;
        }
    }
    protected void createAndShowUpdateTreeFormView() {
        View newsView = ViewFactory.createView("UpdateTreeFormView", this);
        assert newsView != null;
        Scene newsScene = new Scene(newsView);
        swapToView(newsScene);
    }
}