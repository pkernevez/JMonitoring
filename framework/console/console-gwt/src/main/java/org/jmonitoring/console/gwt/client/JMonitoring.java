package org.jmonitoring.console.gwt.client;

import org.jmonitoring.console.gwt.client.images.ConsoleImageBundle;
import org.jmonitoring.console.gwt.client.main.Controller;
import org.jmonitoring.console.gwt.client.main.Menu;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class JMonitoring implements EntryPoint
{
    private HorizontalPanel mHeader;

    private ConsoleImageBundle mBundle;

    private VerticalPanel mMenu;

    private SimplePanel mContent;

    private DockPanel mMain;

    private static JMonitoring sEntry;

    public static String SERVICE_URL = "../ExecutionFlow";

    /**
     * This is the entry point method.
     */
    public void onModuleLoad()
    {
        sEntry = this;
        History.addHistoryListener(new Controller());
        mBundle = GWT.create(ConsoleImageBundle.class);

        mMain = new DockPanel();
        mHeader = new HorizontalPanel();
        mHeader.setStyleName("header");
        mHeader.add(mBundle.jmonitoring().createImage());
        Hyperlink tAbout = new Hyperlink("?", null);
        tAbout.setTitle("About...");
        tAbout.setStyleName("header-right");
        mHeader.add(tAbout);

        mMain.add(mHeader, DockPanel.NORTH);
        mMenu = new Menu();
        mMain.add(mMenu, DockPanel.WEST);
        mMain.setCellHeight(mMenu, "100%");
        mMain.setStyleName("main");

        mContent = new SimplePanel();
        mMain.add(mContent, DockPanel.EAST);
        mMain.setCellHeight(mContent, "100%");
        mMain.setCellWidth(mContent, "100%");

        RootPanel.get().add(mMain);
        History.fireCurrentHistoryState();
    }

    public static void setContentMain(Widget pWidget)
    {
        sEntry.mContent.clear();
        sEntry.mContent.add(pWidget);
    }

    public static Widget getContentMain()
    {
        return sEntry.mContent.getWidget();
    }

    public static ConsoleImageBundle getImageBundle()
    {
        return sEntry.mBundle;
    }

    public static void setWaitCursor(Widget pButton)
    {
        RootPanel.get().addStyleName("wait");
        pButton.addStyleName("wait");
    }

    public static void removeWaitCursor(Widget pButton)
    {
        RootPanel.get().removeStyleName("wait");
        pButton.removeStyleName("wait");
    }
}
