import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.TransferHandler;


public class ListItemTransferHandler extends TransferHandler {
    private int[] indices = null;
    private int addIndex = -1;
    private int addCount = 0;


    @Override
    protected Transferable createTransferable(JComponent c) {
        JList<String> source = (JList<String>) c;
        indices = source.getSelectedIndices();
        Object[] transferredObjects = source.getSelectedValuesList().toArray();
        return new TransferHandlerData(transferredObjects);
    }


    @Override
    public boolean canImport(TransferSupport info) {
        return info.isDataFlavorSupported(DataFlavor.stringFlavor);
    }


    @Override
    public int getSourceActions(JComponent c) {
        return MOVE;
    }


    @Override
    public boolean importData(TransferSupport info) {
        if (!canImport(info)) {
            return false;
        }


        JList<String> target = (JList<String>) info.getComponent();
        JList.DropLocation dl = (JList.DropLocation) info.getDropLocation();
        DefaultListModel<String> listModel = (DefaultListModel<String>) target.getModel();
        int index = dl.getIndex();
        int max = listModel.getSize();
        if (index < 0 || index > max) {
            index = max;
        }
        addIndex = index;


        try {
            String[] values = (String[]) info.getTransferable().getTransferData(DataFlavor.stringFlavor);
            for (int i = 0; i < values.length; i++) {
                int idx = index++;
                listModel.add(idx, values[i]);
                target.addSelectionInterval(idx, idx);
            }
            addCount = values.length;
            return true;
        } catch (UnsupportedFlavorException | IOException ex) {
            ex.printStackTrace();
        }


        return false;
    }


    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
        cleanup(c, action == MOVE);
    }


    private void cleanup(JComponent c, boolean remove) {
        if (remove && indices != null) {
            JList source = (JList) c;
            DefaultListModel model = (DefaultListModel) source.getModel();
            if (addCount > 0) {
                for (int i = 0; i < indices.length; i++) {
                    if (indices[i] >= addIndex) {
                        indices[i] += addCount;
                    }
                }
            }
            for (int i = indices.length - 1; i >= 0; i--) {
                model.remove(indices[i]);
            }
        }
        indices = null;
        addCount = 0;
        addIndex = -1;
    }


    private static class TransferHandlerData implements Transferable {
        private final String[] data;


        public TransferHandlerData(Object[] data) {
            this.data = Arrays.copyOf(data, data.length, String[].class);
        }


        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { DataFlavor.stringFlavor };
        }


        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return DataFlavor.stringFlavor.equals(flavor);
        }


        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return data;
        }
    }
}
