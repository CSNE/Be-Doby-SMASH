package helpers;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClipboardHelper {
        public static void placeFileToClipboard(String path) {
            File file = new File(path);
            List listOfFiles = new ArrayList();
            listOfFiles.add(file);

            FileTransferable ft = new FileTransferable(listOfFiles);

            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ft, new ClipboardOwner() {
                @Override
                public void lostOwnership(Clipboard clipboard, Transferable contents) {
                    System.out.println("Lost ownership");
                }
            });
        }

        public static class FileTransferable implements Transferable {

            private List listOfFiles;

            public FileTransferable(List listOfFiles) {
                this.listOfFiles = listOfFiles;
            }

            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{DataFlavor.javaFileListFlavor};
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return DataFlavor.javaFileListFlavor.equals(flavor);
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                return listOfFiles;
            }
        }


}
