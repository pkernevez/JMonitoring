package org.jmonitoring.console.gwt.client.panel.methodcall;

import static org.jmonitoring.console.gwt.client.panel.PanelUtil.*;

import org.jmonitoring.console.gwt.client.JMonitoring;
import org.jmonitoring.console.gwt.client.dto.RootMethodCallDTO;
import org.jmonitoring.console.gwt.client.main.Controller;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class EditMethodCallPanel extends VerticalPanel
{
    private static final String VIEW_FULL_FLOW = "View full flow...";

    private static final String VIEW_STAT_METH = "View stats...";

    private static final String VIEW_EDIT_METH = "View parent...";

    private static final String VIEW_PREVIOUS_METH_IN_GROUP = "View previous method call of the same group...";

    private static final String VIEW_NEXT_METH_IN_GROUP = "View next method call of the same group...";

    private static final String VIEW_PREVIOUS_METH = "View previous method call...";

    private static final String VIEW_NEXT_METH = "View next method call...";

    private final JMonitoring mMain;

    private final RootMethodCallDTO mMethod;

    public EditMethodCallPanel(JMonitoring pMain, RootMethodCallDTO pMethod)
    {
        super();
        mMain = pMain;
        mMethod = pMethod;
        add(createTitle("Method Call Details"));
        add(createSubTitle(mMethod.getClassName() + "." + mMethod.getMethodName()));

        FlexTable tFlexTable = new FlexTable();
        int curLine = 0;
        tFlexTable.setWidget(curLine, 0, createLabel("Runtime class name:"));
        tFlexTable.getFlexCellFormatter().setWordWrap(curLine, 0, false);
        tFlexTable.setWidget(curLine, 1, createData(mMethod.getRuntimeClassName()));
        tFlexTable.setWidget(++curLine, 0, createLabel("Duration:"));
        tFlexTable.getFlexCellFormatter().setWordWrap(curLine, 0, false);
        tFlexTable.setWidget(curLine, 1, createData(mMethod.getDuration()));
        tFlexTable.setWidget(++curLine, 0, createLabel("Group name:"));
        tFlexTable.getFlexCellFormatter().setWordWrap(curLine, 0, false);
        HorizontalPanel tPanel = new HorizontalPanel();
        HTML tGroupColor =
            new HTML("<DIV style=\"WIDTH: 50px; HEIGHT: 20px; background-color: " + pMethod.getGroupColor()
                + "\"></DIV>");
        tPanel.add(tGroupColor);
        tPanel.add(createData(mMethod.getGroupName()));
        tFlexTable.setWidget(curLine, 1, tPanel);

        tFlexTable.setWidget(++curLine, 0, createLabel("Flow id:"));
        tFlexTable.getFlexCellFormatter().setWordWrap(curLine, 0, false);
        String tFlowHistoryToken = Controller.HISTORY_EDIT_FLOW + mMethod.getFlowId();
        tPanel = new HorizontalPanel();
        tPanel.add(createHyperLink("" + mMethod.getFlowId(), tFlowHistoryToken, VIEW_FULL_FLOW));
        tPanel.add(createClickImage(mMain.getImageBundle().edit(), VIEW_FULL_FLOW, tFlowHistoryToken));
        tFlexTable.setWidget(curLine, 1, tPanel);

        String tStatHistoryToken = Controller.HISTORY_STAT_METH + mMethod.getFlowId() + "&" + mMethod.getPosition();
        tPanel = new HorizontalPanel();
        tFlexTable.setWidget(++curLine, 0, createLabel("Id:"));
        tFlexTable.getFlexCellFormatter().setWordWrap(curLine, 0, false);
        tPanel.add(createHyperLink("" + mMethod.getPosition(), tStatHistoryToken, VIEW_STAT_METH));
        tPanel.add(createClickImage(mMain.getImageBundle().graphique(), VIEW_STAT_METH, tStatHistoryToken));
        tFlexTable.setWidget(curLine, 1, tPanel);

        tFlexTable.setWidget(++curLine, 0, createLabel("Begin date:"));
        tFlexTable.getFlexCellFormatter().setWordWrap(curLine, 0, false);
        tFlexTable.setWidget(curLine, 1, createData(mMethod.getBeginTimeString()));
        tFlexTable.setWidget(++curLine, 0, createLabel("End date:"));
        tFlexTable.getFlexCellFormatter().setWordWrap(curLine, 0, false);
        tFlexTable.setWidget(curLine, 1, createData(mMethod.getEndTimeString()));

        if (mMethod.getParent() != null)
        {
            String tParentHistoryToken =
                Controller.HISTORY_EDIT_METH + mMethod.getFlowId() + "&" + mMethod.getParent().getPosition();
            tPanel = new HorizontalPanel();
            tFlexTable.setWidget(++curLine, 0, createLabel("Parent id:"));
            tFlexTable.getFlexCellFormatter().setWordWrap(curLine, 0, false);
            tPanel.add(createHyperLink("" + mMethod.getParent().getPosition(), tParentHistoryToken, VIEW_EDIT_METH));
            tPanel.add(createClickImage(mMain.getImageBundle().edit(), VIEW_EDIT_METH, tParentHistoryToken));
            tFlexTable.setWidget(curLine, 1, tPanel);
        }

        String tNavHistoryToken = Controller.HISTORY_EDIT_METH + mMethod.getFlowId() + "&";
        tPanel = new HorizontalPanel();
        tFlexTable.setWidget(++curLine, 0, createLabel("Navigation:"));
        tPanel.add(createClickMethod(mMain.getImageBundle().prevInGroup(), VIEW_PREVIOUS_METH_IN_GROUP,
                                     tNavHistoryToken, mMethod.getPrevInGroup()));
        tPanel.add(createClickMethod(mMain.getImageBundle().prevInThread(), VIEW_PREVIOUS_METH, tNavHistoryToken,
                                     mMethod.getPrev()));
        tPanel.add(createClickMethod(mMain.getImageBundle().nextInThread(), VIEW_NEXT_METH, tNavHistoryToken,
                                     mMethod.getNext()));
        tPanel.add(createClickMethod(mMain.getImageBundle().nextInGroup(), VIEW_NEXT_METH_IN_GROUP, tNavHistoryToken,
                                     mMethod.getNextInGroup()));
        tFlexTable.setWidget(curLine, 1, tPanel);

        tFlexTable.setWidget(++curLine, 0, createLabel("Parameters:"));
        tFlexTable.setWidget(curLine, 1, createText(mMethod.getParams()));
        tFlexTable.getFlexCellFormatter().setVerticalAlignment(curLine, 0, HasVerticalAlignment.ALIGN_TOP);

        if (mMethod.getReturnValue() != null)
        {
            tFlexTable.setWidget(++curLine, 0, createLabel("Result:"));
            tFlexTable.getFlexCellFormatter().setVerticalAlignment(curLine, 0, HasVerticalAlignment.ALIGN_TOP);
            tFlexTable.setWidget(curLine, 1, createText(mMethod.getReturnValue()));
        } else
        {
            tFlexTable.setWidget(++curLine, 0, createLabel("Throwable class name:"));
            tFlexTable.setWidget(curLine, 1, createText(mMethod.getThrowableClassName()));
            tFlexTable.setWidget(++curLine, 0, createLabel("Throwable message:"));
            tFlexTable.setWidget(curLine, 1, createText(mMethod.getThrowableMessage()));
        }
        add(tFlexTable);
    }

    private Widget createClickMethod(AbstractImagePrototype pImage, String pViewPreviousMethInGroup,
        String pNavHistoryToken, String pTargetMethod)
    {
        if (pTargetMethod == null)
        {
            return pImage.createImage();
        } else
        {
            return createClickImage(pImage, VIEW_PREVIOUS_METH_IN_GROUP, pNavHistoryToken + pTargetMethod);
        }
    }
}
