package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class TreeTableModel
{
    private final SimpleStringProperty barcode;
    private final SimpleStringProperty treeType;
    private final SimpleStringProperty status;
    private final SimpleStringProperty notes;
    private final SimpleStringProperty dateStatusUpdated;


    //----------------------------------------------------------------------------
    public TreeTableModel(Vector<String> treeData)
    {
        barcode = new SimpleStringProperty(treeData.elementAt(0));
        treeType =  new SimpleStringProperty(treeData.elementAt(1));
        status =  new SimpleStringProperty(treeData.elementAt(2));
        notes =  new SimpleStringProperty(treeData.elementAt(3));
        dateStatusUpdated =  new SimpleStringProperty(treeData.elementAt(4));

    }
    //----------------------------------------------------------------------------
    public String getbarcode() {
        return barcode.get();
    }

    //----------------------------------------------------------------------------
    public void setbarcode(String fn) { barcode.set(fn);}
    //----------------------------------------------------------------------------
    public String getTreeType() {return treeType.get(); }

    //----------------------------------------------------------------------------
    public void setTreeType(String fn) {treeType.set(fn);}

    //----------------------------------------------------------------------------
    public String getStatus() {return status.get();}

    //----------------------------------------------------------------------------
    public void setStatus(String ln) {status.set(ln); }

    //----------------------------------------------------------------------------
    public String getNotes() {return notes.get();}

    //----------------------------------------------------------------------------
    public void setNotes(String mn) {notes.set(mn);}

    //----------------------------------------------------------------------------
    public String getDateStatusUpdated() {return dateStatusUpdated.get();}

    //----------------------------------------------------------------------------
    public void setDateStatusUpdated(String dob)  { dateStatusUpdated.set(dob);}

}
