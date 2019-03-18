import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import java.io.*;
import java.awt.datatransfer.*;
import java.awt.Toolkit;
import external.swing.*;

class Notepad extends JFrame implements ActionListener {

	private JTextArea textArea = new JTextArea();
	private UndoManager undoManager = new UndoManager();

	private MenuBar menuBar = new MenuBar();
	private Menu file = new Menu();
	private Menu edit = new Menu();

	private MenuItem openFile = new MenuItem();
	private MenuItem saveFile = new MenuItem();
	private MenuItem close = new MenuItem();
	private MenuItem selectFont = new MenuItem();
	private MenuItem cut = new MenuItem();
	private MenuItem copy = new MenuItem();
	private MenuItem paste = new MenuItem();
	private MenuItem selectAll = new MenuItem();
	private MenuItem undo = new MenuItem();
	private MenuItem redo = new MenuItem();

	public Notepad() {
		this.setSize(500, 300);
		this.textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
			public void undoableEditHappened(UndoableEditEvent e) {
				undoManager.addEdit(e.getEdit());
			}
		});
		this.setTitle("Java Notepad Digital Assignment 1");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.textArea.setFont(new Font("Product Sans", Font.PLAIN, 12));
		this.getContentPane().setLayout(new BorderLayout()); // the BorderLayout makes it fill it automatically
		this.getContentPane().add(textArea);
		JScrollPane sp = new JScrollPane(this.textArea);
		this.getContentPane().add(sp);

		// add menu bar into GUI
		this.setMenuBar(this.menuBar);
		this.menuBar.add(this.file);
		this.menuBar.add(this.edit);

		this.file.setLabel("File");
		this.edit.setLabel("Edit");

		this.openFile.setLabel("Open");
		this.openFile.addActionListener(this);
		this.openFile.setShortcut(new MenuShortcut(KeyEvent.VK_O, false));
		this.file.add(this.openFile);

		this.saveFile.setLabel("Save");
		this.saveFile.addActionListener(this);
		this.saveFile.setShortcut(new MenuShortcut(KeyEvent.VK_S, false));
		this.file.add(this.saveFile);

		this.close.setLabel("Close");
		this.close.setShortcut(new MenuShortcut(KeyEvent.VK_F4, false));
		this.close.addActionListener(this);
		this.file.add(this.close);

		this.selectFont.setLabel("Select font");
		this.selectFont.addActionListener(this);
		this.edit.add(this.selectFont);

		this.cut.setLabel("Cut");
		this.cut.setShortcut(new MenuShortcut(KeyEvent.VK_X, false));
		this.cut.addActionListener(this);
		this.edit.add(this.cut);

		this.copy.setLabel("Copy");
		this.copy.setShortcut(new MenuShortcut(KeyEvent.VK_C, false));
		this.copy.addActionListener(this);
		this.edit.add(this.copy);

		this.paste.setLabel("Paste");
		this.paste.setShortcut(new MenuShortcut(KeyEvent.VK_V, false));
		this.paste.addActionListener(this);
		this.edit.add(this.paste);

		this.selectAll.setLabel("Select All");
		this.selectAll.setShortcut(new MenuShortcut(KeyEvent.VK_A, false));
		this.selectAll.addActionListener(this);
		this.edit.add(this.selectAll);

		this.undo.setLabel("Undo");
		this.undo.setShortcut(new MenuShortcut(KeyEvent.VK_Z, false));
		this.undo.addActionListener(this);
		this.edit.add(this.undo);

		this.redo.setLabel("Redo");
		this.redo.setShortcut(new MenuShortcut(KeyEvent.VK_Y, false));
		this.redo.addActionListener(this);
		this.edit.add(this.redo);

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == this.close)
			this.dispose();

		else if (e.getSource() == this.openFile) {
			JFileChooser open = new JFileChooser();
			int option = open.showOpenDialog(this);
			if (option == JFileChooser.APPROVE_OPTION) {
				this.textArea.setText("");
				try {

					Scanner scan = new Scanner(new FileReader(open.getSelectedFile().getPath()));
					while (scan.hasNext())
						this.textArea.append(scan.nextLine() + "\n");
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		}

		else if (e.getSource() == this.saveFile) {
			JFileChooser save = new JFileChooser();
			int option = save.showSaveDialog(this);
			if (option == JFileChooser.APPROVE_OPTION) {
				try {

					BufferedWriter out = new BufferedWriter(new FileWriter(save.getSelectedFile().getPath()));
					out.write(this.textArea.getText());
					out.close();
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		}

		else if (e.getSource() == this.selectFont) {
			JFontChooser fontChooser = new JFontChooser();
			int result = fontChooser.showDialog(this);
			if (result == JFontChooser.OK_OPTION) {
				Font font = fontChooser.getSelectedFont();
				System.out.println("Selected Font : " + font);
				this.textArea.setFont(font);
			}
		}

		else if (e.getSource() == this.copy) {
			String text;
			text = this.textArea.getSelectedText();
			if (text == null || text.length() == 0) {
				// copy entire text
				this.textArea.selectAll();
				text = this.textArea.getText();
			}
			StringSelection stringSelection = new StringSelection(text);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);
		}

		else if (e.getSource() == this.selectAll) {
			this.textArea.selectAll();
		}

		else if (e.getSource() == this.paste) {
			try {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				String text = (String) clipboard.getData(DataFlavor.stringFlavor);
				this.textArea.append(text);
			} catch (UnsupportedFlavorException ufe) {
				System.out.println(ufe.toString());
			} catch (IOException io) {
				System.out.println(io.toString());
			}
		}

		else if (e.getSource() == this.cut) {
			String text = this.textArea.getSelectedText();
			if (text.length() == 0)
				return;
			int start = this.textArea.getSelectionStart();
			int end = this.textArea.getSelectionEnd();
			this.textArea.replaceRange("", start, end);
			StringSelection stringSelection = new StringSelection(text);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);
		}

		else if (e.getSource() == this.undo) {
			this.undoManager.undo();
		}

		else if (e.getSource() == this.redo) {
			this.undoManager.redo();
		}

	}
}

class NotepadTest {
	public static void main(String args[]) {
		Notepad app = new Notepad();
		app.setVisible(true);
	}

}
