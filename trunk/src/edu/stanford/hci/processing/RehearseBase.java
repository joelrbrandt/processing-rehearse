package edu.stanford.hci.processing;

import java.awt.Frame;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import edu.stanford.hci.processing.editor.RehearseEditor;
import processing.app.Base;
import processing.app.Editor;
import processing.app.Preferences;
import processing.app.Theme;
import processing.core.PApplet;

public class RehearseBase extends Base {

	public RehearseBase(String[] args) {
		super(args);
	}
	
	protected Editor handleOpen(String path, int[] location) {
	    File file = new File(path);
	    if (!file.exists()) return null;

	    // Cycle through open windows to make sure that it's not already open.
	    for (Editor editor : editors) {
	      if (editor.getSketch().getMainFilePath().equals(path)) {
	        editor.toFront();
	        return editor;
	      }
	    }

	    Editor editor = new RehearseEditor(this, path, location);

	    // Make sure that the sketch actually loaded
	    if (editor.getSketch() == null) {
	      return null;  // Just walk away quietly
	    }

	    editors.add(editor);

	    // now that we're ready, show the window
	    // (don't do earlier, cuz we might move it based on a window being closed)
	    editor.setVisible(true);

	    return editor;
	  }

	
	 static public void main(String args[]) {

	    try {
	      File versionFile = getContentFile("lib/version.txt");
	      if (versionFile.exists()) {
	        VERSION_NAME = PApplet.loadStrings(versionFile)[0];
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	    }

//	    if (System.getProperty("mrj.version") != null) {
//	      //String jv = System.getProperty("java.version");
//	      String ov = System.getProperty("os.version");
//	      if (ov.startsWith("10.5")) {
//	        System.setProperty("apple.laf.useScreenMenuBar", "true");
//	      }
//	    }

	    /*
	    commandLine = false;
	    if (args.length >= 2) {
	      if (args[0].startsWith("--")) {
	        commandLine = true;
	      }
	    }

	    if (PApplet.javaVersion < 1.5f) {
	      //System.err.println("no way man");
	      Base.showError("Need to install Java 1.5",
	                     "This version of Processing requires    \n" +
	                     "Java 1.5 or later to run properly.\n" +
	                     "Please visit java.com to upgrade.", null);
	    }
	    */

	    initPlatform();

//	    // Set the look and feel before opening the window
//	    try {
//	      platform.setLookAndFeel();
//	    } catch (Exception e) {
//	      System.err.println("Non-fatal error while setting the Look & Feel.");
//	      System.err.println("The error message follows, however Processing should run fine.");
//	      System.err.println(e.getMessage());
//	      //e.printStackTrace();
//	    }

	    // Use native popups so they don't look so crappy on osx
	    JPopupMenu.setDefaultLightWeightPopupEnabled(false);

	    // Don't put anything above this line that might make GUI,
	    // because the platform has to be inited properly first.

	    // Make sure a full JDK is installed
	    initRequirements();

	    // run static initialization that grabs all the prefs
	    Preferences.init(null);

	    // setup the theme coloring fun
	    Theme.init();

	    if (Base.isMacOS()) {
	      String properMenuBar = "apple.laf.useScreenMenuBar";
	      String menubar = Preferences.get(properMenuBar);
	      if (menubar != null) {
	        // Get the current menu bar setting and use it
	        System.setProperty(properMenuBar, menubar);

	      } else {
	        // 10.4 is not affected, 10.5 (and prolly 10.6) are
	        if (System.getProperty("os.version").startsWith("10.4")) {
	          // Don't bother checking next time
	          Preferences.set(properMenuBar, "true");
	          // Also set the menubar now
	          System.setProperty(properMenuBar, "true");

	        } else {
	          // Running 10.5 or 10.6 or whatever, give 'em the business
	          String warning =
	            "<html>" +
	            "<head> <style type=\"text/css\">"+
	            "b { font: 13pt \"Lucida Grande\" }"+
	            "p { font: 11pt \"Lucida Grande\"; margin-top: 8px }"+
	            "</style> </head> <body>" +
	            "<b>The standard menu bar has been disabled.</b>" +
	            "<p>Due to an Apple bug, the Processing menu bar " +
	            "is unusable on Mac OS X 10.5. <br>" +
	            "As a workaround, the menu bar will be placed inside " +
	            "the editor window. This <br>setting can be changed in the " +
	            "Preferences window. If this bug makes you sad, <br>" +
	            "please contact Apple via bugreporter.apple.com.</p>" +
	            "</body> </html>";
	          Object[] options = { "OK", "More Info" };
	          int result = JOptionPane.showOptionDialog(new Frame(),
	                                                    warning,
	                                                    "Menu Bar Problem",
	                                                    JOptionPane.YES_NO_OPTION,
	                                                    JOptionPane.WARNING_MESSAGE,
	                                                    null,
	                                                    options,
	                                                    options[0]);
	          if (result == -1) {
	            // They hit ESC or closed the window, so just hide it for now
	            // But don't bother setting the preference in the file
	          } else {
	            // Shut off in the preferences for next time
	            Preferences.set(properMenuBar, "false");
	            if (result == 1) {  // More Info
	              Base.openURL("http://dev.processing.org/bugs/show_bug.cgi?id=786");
	            }
	          }
	          // Whether or not canceled, set to false (right now) if we're on 10.5
	          System.setProperty(properMenuBar, "false");
	        }
	      }
	    }

	    // Set the look and feel before opening the window
	    // For 0158, moving it lower so that the apple.laf.useScreenMenuBar stuff works
	    try {
	      platform.setLookAndFeel();
	    } catch (Exception e) {
	      System.err.println("Non-fatal error while setting the Look & Feel.");
	      System.err.println("The error message follows, however Processing should run fine.");
	      System.err.println(e.getMessage());
	      //e.printStackTrace();
	    }

	    // Create a location for untitled sketches
	    untitledFolder = createTempFolder("untitled");
	    untitledFolder.deleteOnExit();

	    new RehearseBase(args);
	  }
}
