package com.vuexplugin.vuexplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import org.apache.commons.lang.WordUtils;
import org.jetbrains.annotations.NotNull;

public class VueXAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
      /*  Editor editor = e.getData(PlatformDataKeys.EDITOR);
        ActionDialogVueX dialog = new ActionDialogVueX(editor);
        JBPopupFactory.getInstance().createListPopup(dialog, 5).showInFocusCenter();*/

         /*val message = "Scratch name (you can use '&' for mnemonics):"
        val scratchName = Messages.showInputDialog(message, "New Scratch", noIcon, suggestedScratchName, object: InputValidatorEx {
            override fun checkInput(scratchName: String) = mrScratchManager().checkIfUserCanCreateScratchWithName(scratchName).isYes
            override fun getErrorText(scratchName: String) = mrScratchManager().checkIfUserCanCreateScratchWithName(scratchName).explanation
            override fun canClose(inputString: String) = true
        }) ?: return*/
        String message = "Specify state name";

        var stateName = Messages.showInputDialog(message, "Vuex", null);
        System.out.println(stateName);

        var document = e.getData(PlatformDataKeys.EDITOR).getDocument();
        var text = new StringBuilder(document.getText());
        //
        var indexOfState = text.indexOf("state");

        if (indexOfState == -1) {
            var indexOfNamespace = text.indexOf("namespaced") + 10;

            while (text.charAt(indexOfNamespace) != ',') {
                indexOfNamespace++;
            }

            text.insert(indexOfNamespace, "\n\tstate: {\n\t\t" + stateName + ": null,\n\t}");
        } else {
            indexOfState += 5;

            while (text.charAt(indexOfState) != '{') {
                indexOfState++;
            }

            indexOfState++;
            var bracketsAmount = 1;

            while (bracketsAmount != 0) {
                if (text.charAt(indexOfState) == '{') {
                    bracketsAmount++;
                } else if (text.charAt(indexOfState) == '}') {
                    bracketsAmount--;
                }

                indexOfState++;
            }

            indexOfState--;

            text.insert(indexOfState, "\t" + stateName + ": null,\n\t");
        }
        //

        var indexOfGetters = text.indexOf("getters");

        indexOfGetters += 7;

        while (text.charAt(indexOfGetters) != '{') {
            indexOfGetters++;
        }

        indexOfGetters++;
        var bracketsAmount = 1;

        while (bracketsAmount != 0) {
            if (text.charAt(indexOfGetters) == '{') {
                bracketsAmount++;
            } else if (text.charAt(indexOfGetters) == '}') {
                bracketsAmount--;
            }

            indexOfGetters++;
        }

        indexOfGetters--;

        text.insert(indexOfGetters, "\tget" + WordUtils.capitalize(stateName) + " (state) {\n\t\t\treturn state." + stateName + ";\n\t\t},\n\t");
//
        var indexOfMutations = text.indexOf("mutations");

        indexOfMutations += 9;

        while (text.charAt(indexOfMutations) != '{') {
            indexOfMutations++;
        }

        indexOfMutations++;
        bracketsAmount = 1;

        while (bracketsAmount != 0) {
            if (text.charAt(indexOfMutations) == '{') {
                bracketsAmount++;
            } else if (text.charAt(indexOfMutations) == '}') {
                bracketsAmount--;
            }

            indexOfMutations++;
        }

        indexOfMutations--;

        text.insert(indexOfMutations, "\tset" + WordUtils.capitalize(stateName) + " (state, value) {\n\t\t\tstate." + stateName + " = value;\n\t\t},\n\t");
//
        var indexOfActions = text.indexOf("actions");

        indexOfActions += 7;

        while (text.charAt(indexOfActions) != '{') {
            indexOfActions++;
        }

        indexOfActions++;
        bracketsAmount = 1;

        while (bracketsAmount != 0) {
            if (text.charAt(indexOfActions) == '{') {
                bracketsAmount++;
            } else if (text.charAt(indexOfActions) == '}') {
                bracketsAmount--;
            }

            indexOfActions++;
        }

        indexOfActions--;

        text.insert(indexOfActions, "\tfetch" + WordUtils.capitalize(stateName) + " ({commit}) {\n\t\t\tcommit('set" + WordUtils.capitalize(stateName) + "');\n\t\t},\n\t");

        document.setText(text.toString());
    }
}
