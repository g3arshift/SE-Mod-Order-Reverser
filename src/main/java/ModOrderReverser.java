import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class ModOrderReverser {

    private ArrayList<String> modList;
    private String sandboxText;
    private String preModText;
    private String[] modText;
    private File sandboxFile;

    public ModOrderReverser(File sandboxFile) throws IOException {
        this.sandboxFile = sandboxFile;
        modList = new ArrayList<>();
        sandboxText = Files.readString(sandboxFile.toPath());
        preModText = StringUtils.substringBefore(sandboxText, "<Mods>");
        modText = StringUtils.substringAfter(sandboxText, "<Mods>").split(System.getProperty("line.separator"));
    }

    public String ReverseModOrder() throws IOException {
        //Notes:
        // Store file in 2 parts. Pre mods text, and post mods text.
        // Ask the user where the file is
        // Modify file
        // Ask the user where they want to save the file.

        String modBlock = "";
        for (int i = 1; i < modText.length - 4; i++) {
            modBlock = modBlock + modText[i] + "\n";
            if (i % 5 == 0) {
                modList.add(modBlock);
                modBlock = "";
            }
        }

        String completeSandboxFile = preModText + "<Mods>\n";

        for (int i = modList.size() - 1; i > 0; i--) {
            completeSandboxFile = completeSandboxFile + modList.get(i);
        }

        completeSandboxFile = completeSandboxFile + "  </Mods>" + "\n" + modText[modText.length - 3] + "\n" + modText[modText.length - 2] + "\n" + "</MyObjectBuilder_WorldConfiguration>";
        return completeSandboxFile;
    }
}
