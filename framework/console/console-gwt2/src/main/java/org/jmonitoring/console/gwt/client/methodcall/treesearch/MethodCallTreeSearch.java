package org.jmonitoring.console.gwt.client.methodcall.treesearch;

import org.jmonitoring.console.gwt.client.resources.ConsoleImageBundle;
import org.jmonitoring.console.gwt.shared.method.treesearch.ClassDTO;
import org.jmonitoring.console.gwt.shared.method.treesearch.MethodDTO;
import org.jmonitoring.console.gwt.shared.method.treesearch.PackageDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasTreeItems;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class MethodCallTreeSearch extends Composite
{

    private static MethodCallTreeUiBinder uiBinder = GWT.create(MethodCallTreeUiBinder.class);

    private static ConsoleImageBundle ressource = GWT.create(ConsoleImageBundle.class);

    @UiField
    Tree tree;

    interface MethodCallTreeUiBinder extends UiBinder<Widget, MethodCallTreeSearch>
    {
    }

    public MethodCallTreeSearch()
    {
        initWidget(uiBinder.createAndBindUi(this));
        Window.setTitle("Method call tree search");
    }

    private MethodCallTreeSearchActivity presenter;

    public MethodCallTreeSearch setPresenter(MethodCallTreeSearchActivity pActivity)
    {
        presenter = pActivity;
        return this;
    }

    public void buildRoot(PackageDTO pPackage)
    {
        for (PackageDTO curChild : pPackage.getSubPackages())
        {
            buildNode(tree, curChild);
        }
        for (ClassDTO curClass : pPackage.getClasses())
        {
            buildNode(tree, curClass);
        }
    }

    private void buildNode(HasTreeItems pParent, PackageDTO pPackage)
    {
        TreeItem curWidget = pParent.addItem(new HTML(pPackage.getName()));
        for (PackageDTO curChild : pPackage.getSubPackages())
        {
            buildNode(curWidget, curChild);
        }
        for (ClassDTO curClass : pPackage.getClasses())
        {
            buildNode(curWidget, curClass);
        }
    }

    private void buildNode(HasTreeItems pParent, ClassDTO pClass)
    {
        HasTreeItems curWidget = pParent.addItem(new HTML(pClass.getName()));
        for (MethodDTO curMeth : pClass.getMethods())
        {
            buildNode(curWidget, curMeth);
        }

    }

    private void buildNode(HasTreeItems pParent, MethodDTO pMeth)
    {
        StringBuilder tBuilder = new StringBuilder();
        tBuilder.append(pMeth.getName()).append("    ( ").append(pMeth.getNbOccurence()).append(" )&nbsp;");
        Panel tPanel = new HorizontalPanel();
        tPanel.add(new HTML(tBuilder.toString()));
        tPanel.add(new Image(ressource.graphique()));
        pParent.addItem(tPanel);
    }
}
