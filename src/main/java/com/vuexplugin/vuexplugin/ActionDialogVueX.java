package com.vuexplugin.vuexplugin;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ActionDialogVueX extends BaseListPopupStep<String> {
    Editor editor;
    Document document;

    String selectedValueInState;
    static final ArrayList<String> itemPopUp = new ArrayList<>() {{
        add("Create");
        add("Getters and Setters");
        add("Getters");
        add("Setters");
    }};

    public ActionDialogVueX(Editor editor) {

        super("Choose", itemPopUp);
        this.editor = editor;
    }

    @Override
    public @Nullable PopupStep<?> onChosen(String selectedValue, boolean finalChoice) {
        System.out.println(selectedValue);
        document = editor.getDocument();
        selectedValueInState = editor.getSelectionModel().getSelectedText();
        switch (selectedValue) {
            case "Create": {
                create();
                break;
            }
            case "Getters and Setters": {
                makeGetters();
                makeSetters();
                break;
            }

            case "Getters": {
                makeGetters();
                break;
            }
            case "Setters": {
                makeSetters();
                break;
            }
            default: {
                break;
            }
        }
        if (!selectedValue.equals("Create")) {
            makeState();
        }
        return super.onChosen(selectedValue, finalChoice);
    }


    private void makeState() {
        StringBuilder state = new StringBuilder();
        String documentText = document.getText(), str;
        String[] params;
        if (selectedValueInState.indexOf(',') != -1) {
            params = selectedValueInState.split(",");
        } else {
            params = new String[]{selectedValueInState};
        }

        String fileName = getFileName();
        for (String param : params) {
            str = firstLetterToUpperCase(param);
            state.append(param + ":null,\n\t\t");
        }
        documentText = documentText.replaceAll(selectedValueInState, state.toString());
        document.setText(documentText);
    }

    private void create() {
        String createForm =
                "export default {\n" +
                        "\tnamespaced: true,\n" +
                        "\tstate: {\n" +
                        "\t\t\n" +
                        "\t},\n" +
                        "\tgetters: {\n" +
                        "\t\t\n" +
                        "\t},\n" +
                        "\tmutations: {\n" +
                        "\t\t\n" +
                        "\t},\n" +
                        "\tactions: {\n" +
                        "\t\t\n" +
                        "\t},\n" +
                        "}";
        try {
            document.setText(createForm);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }

    }

    private void makeGetters() {
        StringBuilder
                getters = new StringBuilder(),
                mapGetters = new StringBuilder();
        String documentText = document.getText(), str;
        String[] params;
        if (selectedValueInState.indexOf(',') != -1) {
            params = selectedValueInState.split(",");
        } else {
            params = new String[]{selectedValueInState};
        }
        mapGetters.append("//...mapGetters({\n\t");
        String fileName = getFileName();
        for (String param : params) {
            str = firstLetterToUpperCase(param);
            getters.append("\t\tget" + str + "(state){\n\t\t\treturn state." + param + ";\n\t\t},\n");
            mapGetters.append("//'get" + str + "':'" + fileName + "/get" + str + "',\n\t");
        }
        mapGetters.append("//}),");
        documentText = documentText.replaceAll("//VueXGetters", "//VueXGetters\n" + getters.toString());
        document.setText(documentText + "\n" + mapGetters.toString());
    }

    private void makeSetters() {
        StringBuilder
                mutations = new StringBuilder(),
                mapMutations = new StringBuilder();
        String documentText = document.getText(), str;
        String[] params;
        if (selectedValueInState.indexOf(',') != -1) {
            params = selectedValueInState.split(",");
        } else {
            params = new String[]{selectedValueInState};
        }
        mapMutations.append("//...mapMutations({\n\t");
        String fileName = getFileName();
        for (String param : params) {
            str = firstLetterToUpperCase(param);
            mutations.append("\t\tset" + str + "(state,value){\n\t\t\tstate." + param + " = value;\n\t\t},\n");
            mapMutations.append("//'set" + str + "':'" + fileName + "/set" + str + "',\n\t");
        }
        mapMutations.append("//}),");
        documentText = documentText.replaceAll("//VueXMutations", "//VueXMutations\n" + mutations.toString());
        document.setText(documentText + "\n" + mapMutations.toString());
    }

    private String getFileName() {
        String temp = document.toString();
        temp = temp.substring(temp.lastIndexOf("/") + 1).replaceAll(".js]", "");
        return temp;
    }

    private String firstLetterToUpperCase(String word) {
        if (word == null || word.length() == 0)
            return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
}
