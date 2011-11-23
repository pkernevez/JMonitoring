package org.jmonitoring.console.gwt.client.methodcall.detail;

import org.jmonitoring.console.gwt.client.common.NavHandler;
import org.jmonitoring.console.gwt.client.methodcall.distribution.MethodCallDistributionPlace;
import org.jmonitoring.console.gwt.client.resources.ConsoleImageBundle;
import org.jmonitoring.console.gwt.shared.flow.MethodCallExtractDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * TODO Replace with Cell List : http://gwt.google.com/samples/Showcase/Showcase.html#!CwCellList
 * 
 * @author pke
 * 
 */
public class SubMethodCall extends Composite
{

    private static SubMethodCallUiBinder uiBinder = GWT.create(SubMethodCallUiBinder.class);

    @UiField
    SpanElement prevDuration;

    @UiField
    SpanElement curDuration;

    @UiField
    Anchor description;

    @UiField
    Image edit;

    @UiField
    Image stats;

    @UiField
    Image throwImage;

    @UiField
    ConsoleImageBundle image;

    interface SubMethodCallUiBinder extends UiBinder<Widget, SubMethodCall>
    {
    }

    interface Style extends CssResource
    {
        String mask();
    }

    public SubMethodCall()
    {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public SubMethodCall(MethodCallExtractDTO pChild, String pFlowId)
    {
        initWidget(uiBinder.createAndBindUi(this));
        prevDuration.setInnerHTML("[-&gt;" + pChild.getTimeFromPrevChild() + "]");
        curDuration.setInnerHTML("[" + pChild.getDuration() + "]");
        NavHandler tHandler = new NavHandler(new MethodCallDetailPlace(pFlowId, pChild.getPosition()));
        description.setText(pChild.getGroupName() + " -> " + pChild.getFullMethodName());
        description.addClickHandler(tHandler);
        edit.addClickHandler(tHandler);
        stats.addClickHandler(new NavHandler(new MethodCallDistributionPlace(pFlowId, pChild.getPosition())));
        if (pChild.hasThrownException())
        {
            throwImage.setResource(image.warn());
            throwImage.setTitle("This method thrown an Exception");
        }
    }
}
