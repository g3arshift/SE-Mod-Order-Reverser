import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }


        JFileChooser fc = new JFileChooser(System.getenv("APPDATA") + "/SpaceEngineers/Saves");
        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("SBC Files", "sbc");
        boolean goodFile = false;

        JOptionPane.showMessageDialog(null, "Please select a Sandbox_config.sbc to reverse the mod load order of.", "Notification", JOptionPane.INFORMATION_MESSAGE);
        fc.setFileFilter(fileFilter);

        do {
            int option = fc.showOpenDialog(null);

            if (option == JFileChooser.APPROVE_OPTION) {
                File sandboxFile = fc.getSelectedFile();

                //Error codes
                if (!sandboxFile.exists()) {
                    JOptionPane.showMessageDialog(null, "File does not exist, please select another.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!FilenameUtils.getExtension(sandboxFile.getName()).equals("sbc")) {
                    JOptionPane.showMessageDialog(null, "Incorrect file type selected.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!sandboxFile.getName().equals("Sandbox_config.sbc")) {
                    JOptionPane.showMessageDialog(null, "A file other than Sandbox_config.sbc was selected.", "Error", JOptionPane.ERROR_MESSAGE);
                } else
                    goodFile = true;
            } else
                return;
        } while (!goodFile);

        String completedSandboxFile = new ModOrderReverser(fc.getSelectedFile()).ReverseModOrder();

        JOptionPane.showMessageDialog(null, "Select a location to save the new Sandbox_config.sbc", "Save file", JOptionPane.INFORMATION_MESSAGE);
        fc = new JFileChooser(System.getProperty("user.home" + "/Desktop"));
        fc.setFileFilter(fileFilter);
        int option = fc.showSaveDialog(null);

        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                String savePath = "";
                if (fc.getSelectedFile().toPath().toString().contains(".sbc")) {
                    savePath = fc.getSelectedFile().toPath().toString().replace(".sbc", "");
                    savePath = savePath + ".sbc";
                }
                else
                    savePath = fc.getSelectedFile().toPath().toString() + ".sbc";

                File file = new File(savePath);

                if (!file.exists()) {
                    file.createNewFile();
                }

                FileWriter fw = new FileWriter(file);

                BufferedWriter output = new BufferedWriter(fw);

                output.write(completedSandboxFile);

                output.flush();
                JOptionPane.showMessageDialog(null, "File saved!", "Save Complete", JOptionPane.INFORMATION_MESSAGE);
                output.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (HeadlessException e) {
                throw new RuntimeException(e);
            }
        }
        else
            JOptionPane.showMessageDialog(null, "Mod order reversal cancelled. Your files have not been modified.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
    }
}