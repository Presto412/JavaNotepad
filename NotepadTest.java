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

	// this is the textarea element
	private JTextArea textArea = new JTextArea();
	// For undo implementation this is the class
	private UndoManager undoManager = new UndoManager();

	// this is the menubar
	private MenuBar menuBar = new MenuBar();
	// menu - file
	private Menu file = new Menu();
	// menu - edit
	private Menu edit = new Menu();

	// these are menu-items
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

		// setting the size
		this.setSize(500, 300);

		// adding the undo listener for events
		this.textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
			public void undoableEditHappened(UndoableEditEvent e) {
				undoManager.addEdit(e.getEdit());
			}
		});
		this.setTitle("Java Notepad Digital Assignment 1");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// setting the font
		this.textArea.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		this.getContentPane().setLayout(new BorderLayout()); // the BorderLayout makes it fill it automatically

		// adding the textarea to the pane
		this.getContentPane().add(this.textArea);

		JScrollPane sp = new JScrollPane(this.textArea);
		this.getContentPane().add(sp);

		// add menu bar into GUI
		this.setMenuBar(this.menuBar);

		// add the menus to the menubar
		this.menuBar.add(this.file);
		this.menuBar.add(this.edit);

		// set the labels for the menus
		this.file.setLabel("File");
		this.edit.setLabel("Edit");

		// Setting the label for openFile menuitem
		this.openFile.setLabel("Open");
		// adding action listener
		this.openFile.addActionListener(this);
		// setting shortcut to Ctrl + O
		this.openFile.setShortcut(new MenuShortcut(KeyEvent.VK_O, false));
		// adding the menuitem to the file menu
		this.file.add(this.openFile);

		// similarly the others have been setup

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

		// if close option is clicked
		if (e.getSource() == this.close)
			// closes the application
			this.dispose();

		// if open option is clicked
		else if (e.getSource() == this.openFile) {
			// creates the file chooser
			JFileChooser open = new JFileChooser();
			// opens the file chooser
			int option = open.showOpenDialog(this);

			// if user clicks a file to import
			if (option == JFileChooser.APPROVE_OPTION) {
				this.textArea.setText("");
				try {
					// reading the text in the file line by line
					Scanner scan = new Scanner(new FileReader(open.getSelectedFile().getPath()));
					while (scan.hasNext())
						// adding to the text area
						this.textArea.append(scan.nextLine() + "\n");
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		}

		// save option is clicked
		else if (e.getSource() == this.saveFile) {
			JFileChooser save = new JFileChooser();
			int option = save.showSaveDialog(this);
			if (option == JFileChooser.APPROVE_OPTION) {
				try {
					// writing the content in text area to selected file name
					BufferedWriter out = new BufferedWriter(new FileWriter(save.getSelectedFile().getPath()));
					out.write(this.textArea.getText());
					out.close();
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		}

		// fonts is selected
		else if (e.getSource() == this.selectFont) {
			JFontChooser fontChooser = new JFontChooser();
			// opens the font chooser dialog
			int result = fontChooser.showDialog(this);
			if (result == JFontChooser.OK_OPTION) {
				Font font = fontChooser.getSelectedFont();
				System.out.println("Selected Font : " + font);
				// sets the font
				this.textArea.setFont(font);
			}
		}

		// if copy is sleected
		else if (e.getSource() == this.copy) {
			String text;
			// copy the selected in text variable
			text = this.textArea.getSelectedText();
			// if no text is selected before copying
			if (text == null || text.length() == 0) {
				// copy entire text
				this.textArea.selectAll();
				text = this.textArea.getText();
			}
			StringSelection stringSelection = new StringSelection(text);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			// copy the text in the clipboard
			clipboard.setContents(stringSelection, null);
		}

		// if select all is pressed,
		else if (e.getSource() == this.selectAll) {
			// select everything in the text area
			this.textArea.selectAll();
		}

		// if paste is selected
		else if (e.getSource() == this.paste) {
			try {

				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				// get currrently present text in clipboard
				String text = (String) clipboard.getData(DataFlavor.stringFlavor);
				// append the text to the clipboard
				this.textArea.append(text);
			} catch (UnsupportedFlavorException ufe) {
				System.out.println(ufe.toString());
			} catch (IOException io) {
				System.out.println(io.toString());
			}
		}

		// if cut is selected
		else if (e.getSource() == this.cut) {
			// get text selected
			String text = this.textArea.getSelectedText();
			if (text.length() == 0)
				return;
			// get start and end range of selection
			int start = this.textArea.getSelectionStart();
			int end = this.textArea.getSelectionEnd();

			// remove the selection
			this.textArea.replaceRange("", start, end);
			// copy the removed text from clipboard
			StringSelection stringSelection = new StringSelection(text);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);
		}

		// if undo is selected
		else if (e.getSource() == this.undo) {
			// undo the latest event
			this.undoManager.undo();
		}

		// if redo is selected
		else if (e.getSource() == this.redo) {
			// redo the latest event
			this.undoManager.redo();
		}

	}
}

// class with main method
class NotepadTest {
	public static void main(String args[]) {
		// create object of notepad class that extends JFrame from swing
		Notepad app = new Notepad();
		// makes the app visible to uses
		app.setVisible(true);
	}

}
