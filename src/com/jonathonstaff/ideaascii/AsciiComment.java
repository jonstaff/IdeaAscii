package com.jonathonstaff.ideaascii;

import com.intellij.ide.impl.DataManagerImpl;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
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

		if (project == null) {
			return;
		}

		Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
		if (editor == null) {
			return;
		}

		final Document document = editor.getDocument();

		final int offset = editor.getCaretModel().getOffset();

		final Runnable readRunner = new Runnable() {
			@Override
			public void run() {
				document.insertString(offset, "waaaaaattttt?");
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
