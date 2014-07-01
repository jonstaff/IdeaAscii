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

        // Get the current Project
        final Project project = e.getProject();
        if (project == null) return;

        // Get the editor
        final Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) return;

        // Set the initial value
        String initialValue = "";

        // Get the selection model
        final SelectionModel selectionModel = editor.getSelectionModel();

        // Check if we have selected text
        if (selectionModel.hasSelection()) {

            String selectedText = selectionModel.getSelectedText();

            /* If no text is selected, we will select the whole line,
               and if the line is empty, we'll just return an empty string */
            if (selectedText == null) {

                // Select the line
                selectionModel.selectLineAtCaret();
                selectedText = selectionModel.getSelectedText();

                if (selectedText == null)
                    selectedText = "";

            }

            // Prepare the selected text
            if (!selectedText.isEmpty()) {

                // Trim the selected text
                selectedText = selectedText.trim();

                // If there are many lines in this selected text, only return the first one
                if (selectedText.contains("\n"))
                    selectedText = selectedText.substring(0, selectedText.indexOf("\n"));

            }

            initialValue = selectedText;

        }

        // Show the input dialog
        final String text = Messages.showInputDialog(project, null, "ASCII Text", null, initialValue, null);

        final Runnable readRunner = new Runnable() {
            @Override
            public void run() {

                int offset = editor.getCaretModel().getOffset();

                // Get the document
                final Document document = editor.getDocument();

                // Check if we should replace the selected text
                if (selectionModel.hasSelection()) {

                    // Get the start and end position of the selected text
                    int selectionStart = selectionModel.getSelectionStart();
                    int selectionEnd = selectionModel.getSelectionEnd();

                    // Remove the selection, and delete the string from the document
                    selectionModel.removeSelection();
                    document.deleteString(selectionStart, selectionEnd);

                    // Set the offset to where the selection started
                    offset = selectionStart;

                }

                int lineNumber = document.getLineNumber(offset);
                int lineStartOffset = document.getLineStartOffset(lineNumber);
                int indentLength = offset - lineStartOffset;

                // Insert the string
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
