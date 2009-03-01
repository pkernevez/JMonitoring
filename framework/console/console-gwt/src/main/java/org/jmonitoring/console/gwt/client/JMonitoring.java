package org.jmonitoring.console.gwt.client;

import org.jmonitoring.console.gwt.client.images.ConsoleImageBundle;
import org.jmonitoring.console.gwt.client.main.Menu;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

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

    /**
     * This is the entry point method.
     */
    public void onModuleLoad()
    {
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
        mMenu = new Menu(this);
        mMain.add(mMenu, DockPanel.WEST);
        mMain.setCellHeight(mMenu, "100%");
        mMain.setStyleName("main");

        mContent = new SimplePanel();
        mMain.add(mContent, DockPanel.EAST);
        mMain.setCellHeight(mContent, "100%");
        mMain.setCellWidth(mContent, "100%");

        RootPanel.get().add(mMain);
        // MyServiceAsync svc = (MyServiceAsync) GWT.create(MyService.class);
        // ServiceDefTarget endpoint = (ServiceDefTarget) svc;
        // endpoint.setServiceEntryPoint("/MyService");
        // AsyncCallback<String> callback = new AsyncCallback<String>()
        // {
        // public void onSuccess(String result)
        // {
        // RootPanel.get().add(new HTML(result.toString()));
        // }
        //
        // public void onFailure(Throwable ex)
        // {
        // RootPanel.get().add(new HTML(ex.toString()));
        // }
        // };
        // GWT.log("Avant appel", null);
        // svc.myMethod("Do Stuff", callback);
        // Create an image, not yet referencing a URL. We make it final so that we
        // can manipulate the image object within the ClickHandlers for the buttons.
    }

    public void setContentMain(Panel pPanel)
    {
        mContent.clear();
        mContent.add(pPanel);
    }

    public ConsoleImageBundle getImageBundle()
    {
        return mBundle;
    }

}
