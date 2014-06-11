package com.jonathonstaff.ideaascii;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.jonathonstaff.ideaascii.util.Util;

//  Created by jonstaff on 6/11/14.

public class AsciiComment extends AnAction {

	public void actionPerformed(AnActionEvent e) {
		final Project project = e.getProject();
		if (project == null) {
			return;
		}

        final String txt = Messages.showInputDialog(project, "ASCII text", "ASCII text", Messages.getQuestionIcon());

		Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
		if (editor == null) {
			return;
		}

		final Document document = editor.getDocument();
		final int offset = editor.getCaretModel().getOffset();

		final Runnable readRunner = new Runnable() {
			@Override
			public void run() {
				document.insertString(offset, Util.convertTextToAscii(txt));
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
