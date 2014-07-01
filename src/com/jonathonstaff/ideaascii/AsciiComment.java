package com.jonathonstaff.ideaascii;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.jonathonstaff.ideaascii.util.Util;

//  Created by jonstaff on 6/11/14.
//  Edited by kjarrio on 2014-07-01

public class AsciiComment extends AnAction {

    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getProject();
        if (project == null) return;

        final Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) return;

        String initialValue = "";

        final SelectionModel selectionModel = editor.getSelectionModel();

        if (!selectionModel.hasSelection()) {
            selectionModel.selectLineAtCaret();
        }

        String selectedText = selectionModel.getSelectedText();
        if ((selectedText != null) && (!selectedText.isEmpty())) {
            selectedText = selectedText.trim();

            if (selectedText.contains("\n")) {
                selectedText = selectedText.substring(0, selectedText.indexOf("\n"));
            }

            initialValue = selectedText;
        }

        final String text = Messages.showInputDialog(project, null, "ASCII Text", null, initialValue, null);

        final Runnable readRunner = new Runnable() {
            @Override
            public void run() {
                if (text == null) return;
                int offset = editor.getCaretModel().getOffset();
                final Document document = editor.getDocument();

                // Check if we should replace the selected text
                if (selectionModel.hasSelection()) {
                    int selectionStart = selectionModel.getSelectionStart();
                    int selectionEnd = selectionModel.getSelectionEnd();

                    selectionModel.removeSelection();
                    document.deleteString(selectionStart, selectionEnd);
                    offset = selectionStart;
                }

                int lineNumber = document.getLineNumber(offset);
                int lineStartOffset = document.getLineStartOffset(lineNumber);
                int indentLength = offset - lineStartOffset;

                document.insertString(offset, Util.convertTextToAscii(text, indentLength));
            }
        };

        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                CommandProcessor.getInstance().executeCommand(project, new Runnable() {
                    @Override
                    public void run() {
                        ApplicationManager.getApplication().runWriteAction(readRunner);
                    }
                }, "IdeaAscii", null);
            }
        });
    }
}
