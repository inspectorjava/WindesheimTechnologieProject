/*
Class <code>AbstractFactory</code> implements an example of the Creational Desing Patterns
- AbstractFactory

Please note that the comments are mainly informative and the structure for ease-of-handling.
The usage of the java-sources are to demonstrate the AbstractFactory and Singleton pattern and
and not good java practice/coding.
Used are:
= Type ---- Name ------------------ Implements ----------------------------------------------------------
* Class     AbstractFactoryDemo     Demo usage of the AbstractFactory Pattern.
* Class     GUIBuilder              The builder that is called by the User (in AbstractFactoryDemo)
* Interface Window                  Abstract implementation for setTitle and repaint.
* Class     MSWindow                Implementation for Interface Window for MS-Windows.
* Class     MacOSXWindow            Implementation for Interface Window for Mac-OSX.
* Interface AbstractWidgetFactory   Abstract implementation for createWindow
* Class     MsWindowsWidgetFactory  Implementation for Interface AbstractWidgetFactory for MS-Windows.
* Class     MacOSXWidgetFactory     Implementation for Interface MacOSXWidgetFactory for Mac-OSX.
*
*
* All classes are placed in this single file for simplicity and ease of usage.
*
* See the documentation on https://www.harmfrielink.nl/wiki/index.php/Abstract_Factory.
*/


/**
 * The class <code>AbstractFactoryDemo</code> is build on the client app and uses the AbstractFactory pattern.
 */
public class AbstractFactoryDemo {
    /**
     * Main without usage of the parameters.
     * @param args CLI arguments (not used).
     */
    public static void main(String[] args) throws Exception {
        GUIBuilder builder = new GUIBuilder();
        AbstractWidgetFactory widgetFactory = null;

        String currentPlatform = System.getProperty("os.name");
        System.out.printf("%-20.20s: %s\n", "Current Platform", currentPlatform);

        if (currentPlatform.toLowerCase().indexOf( "mac") != -1) {
            widgetFactory  = new MacOSXWidgetFactory();
        } else if (currentPlatform.toLowerCase().indexOf( "window" ) != -1) {
            widgetFactory  = new MsWindowsWidgetFactory();
        } else {
            throw new Exception("Unknown platform " + currentPlatform);
        }
        builder.buildWindow(widgetFactory);
    }
}   // class AbstractFactory

/**
 * The GUIBuilder Client which is the only entry point.
 * The first main entry point for the client-application uses:
 * - Interface Window.
 */
class GUIBuilder {
    /**
     * Builds the window.
     * @param widgetFactory Implementation for the AbstractWidgetFactory (MacOSXWidgetFactory or MsWindowsWidgetFactory).
     */
    public void buildWindow(AbstractWidgetFactory widgetFactory) {
        // Gets the correct window Implementation.
        Window window = widgetFactory.createWindow();

        // Sets the window.
        window.setTitle("New Window");
    }
}  // class GUIBuilder


/**
 * Abstract product implementation Window.
 */
interface Window {
    /** Sets the title of the window. */
    public void setTitle(String text);

    /** Repaint implementation. */
    public void repaint();
}  // interface Window

/**
 * Concrete product for Microsoft-Windows implementation of Interface Window.
 */
class MSWindow implements Window{

    /**
     * Sets the Title
     * @param  title The name of the title.
     */
    public void setTitle(String title) {
        // MS Windows specific behaviour
        System.out.printf("%-20.20s: %s\n", "MSWindow", title);
    }  // setTitle

    public void repaint() {
        // MS Windows specific behaviour
    }
}  // MSWindow

/**
 * Concrete product for Mac-OSX implementation of Interface Window.
 */
class MacOSXWindow implements Window {

    /**
     * Sets the Title
     * @param  $title The name of the title.
     */
    public void setTitle(String title) {
        // MacOSX specific behaviour
        System.out.printf("%-20.20s: %s\n", "MacOSXWindow", title);
    }  // setTitle

    public void repaint() {
        // MacOSX specific behaviour
    }
}  // MacOSXWindow


/**
 * Abstractfactory for the Window.
 */
interface AbstractWidgetFactory {
    /** Creates the window */
    public Window createWindow();
}  // AbstractWidgetFactory

/** Concrete Factory for MS-Windows. */
class MsWindowsWidgetFactory implements AbstractWidgetFactory {

    public Window createWindow(){
        MSWindow window = new MSWindow();
        return window;
    }  // createWindow
}  // MsWindowsWidgetFactory

/** Concrete Factory for Mac OSX. */
class MacOSXWidgetFactory implements AbstractWidgetFactory {

    public Window createWindow(){
        MacOSXWindow window = new MacOSXWindow();
        return window;
    }  // createWindow
}  // MacOSXWidgetFactory







