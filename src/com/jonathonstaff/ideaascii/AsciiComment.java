package com.jonathonstaff.ideaascii;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//  Created by jonstaff on 6/11/14.

public class AsciiComment extends AnAction {

	public void actionPerformed(AnActionEvent e) {
        System.out.println("action started");
		final Project project = e.getProject();
		String txt = Messages.showInputDialog(project, "What is your name?", "Input your name", Messages.getQuestionIcon());
		Messages.showMessageDialog(project, "Hello, " + txt + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());

		if (project == null) {
			return;
		}

		Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
		if (editor == null) {
			return;
		}

		final Document document = editor.getDocument();

		VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
		if (virtualFile == null) {
			return;
		}
		final String contents;

		try {
			BufferedReader br = new BufferedReader(new FileReader(virtualFile.getPath()));
			String currentLine;
			StringBuilder stringBuilder = new StringBuilder();
			while ((currentLine = br.readLine()) != null) {
				stringBuilder.append(currentLine);
				stringBuilder.append("\n");
			}
			contents = stringBuilder.toString();
		}
		catch (IOException e1) {
			return;
		}

		final Runnable readRunner = new Runnable() {
			@Override
			public void run() {
				document.setText(contents);
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
				}, "DiskRead", null);
			}
		});
	}
}
