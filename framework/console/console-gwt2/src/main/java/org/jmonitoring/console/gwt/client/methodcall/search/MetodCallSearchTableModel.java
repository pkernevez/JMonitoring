package org.jmonitoring.console.gwt.client.methodcall.search;

import it.pianetatecno.gwt.utility.client.table.ActionHandler;
import it.pianetatecno.gwt.utility.client.table.Callback;
import it.pianetatecno.gwt.utility.client.table.Column;
import it.pianetatecno.gwt.utility.client.table.ColumnDefinition;
import it.pianetatecno.gwt.utility.client.table.ColumnString;
import it.pianetatecno.gwt.utility.client.table.PagingTable;
import it.pianetatecno.gwt.utility.client.table.Request;
import it.pianetatecno.gwt.utility.client.table.SerializableResponse;
import it.pianetatecno.gwt.utility.client.table.TableModel;

import java.util.ArrayList;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.common.GwtRemoteServiceAsync;
import org.jmonitoring.console.gwt.client.common.JMonitoringAsyncCallBack;
import org.jmonitoring.console.gwt.client.flow.detail.FlowDetailPlace;
import org.jmonitoring.console.gwt.client.methodcall.detail.MethodCallDetailPlace;
import org.jmonitoring.console.gwt.shared.method.MethodCallSearchCriterion;
import org.jmonitoring.console.gwt.shared.method.MethodCallSearchExtractDTO;

import com.google.gwt.user.client.ui.Image;

public class MetodCallSearchTableModel extends TableModel<MethodCallSearchExtractDTO>
{
    private static final String VIEW_FLOW = "viewFlow";

    private static final String VIEW_METHOD = "viewMethod";

    private final GwtRemoteServiceAsync service;

    private MethodCallSearchCriterion searchCriterion;

    /**
     * Workaround to no do a first call during the creation of the Tqble as will do that just after the filter
     * initialization. This is necessary to handle history.
     */
    private boolean firstTime = true;

    public MetodCallSearchTableModel(GwtRemoteServiceAsync pService)
    {
        super();
        service = pService;
    }

    @Override
    public void requestRows(final Request pRequest, final Callback<MethodCallSearchExtractDTO> pCallback)
    {
        // TODO Remove this hack
        if (firstTime)
        {
            pCallback.onRowsReady(pRequest, createResponse());
            firstTime = false;
        } else
        {
            service.searchMethodCall(pRequest, searchCriterion,
                                     new JMonitoringAsyncCallBack<SerializableResponse<MethodCallSearchExtractDTO>>()
                                     {
                                         public void onSuccess(SerializableResponse<MethodCallSearchExtractDTO> pResult)
                                         {
                                             pCallback.onRowsReady(pRequest, pResult);
                                         }

                                     });
        }
    }

    private SerializableResponse<MethodCallSearchExtractDTO> createResponse()
    {
        SerializableResponse<MethodCallSearchExtractDTO> tResult =
            new SerializableResponse<MethodCallSearchExtractDTO>();
        tResult.setRows(new ArrayList<MethodCallSearchExtractDTO>());
        return tResult;
    }

    public PagingTable<MethodCallSearchExtractDTO> getTable()
    {
        // Add the actions on the table
        PagingTable<MethodCallSearchExtractDTO> tTable =
            new PagingTable<MethodCallSearchExtractDTO>(this, getColumnDefinition(), 10, "flow.id", Column.SORTING_ASC);
        tTable.addActionHandler(new ActionHandler<MethodCallSearchExtractDTO>()
        {
            public void onActionPerformed(String pActionName, MethodCallSearchExtractDTO pRow)
            {
                if (VIEW_FLOW.equals(pActionName))
                {
                    ClientFactory.goTo(new FlowDetailPlace(pRow.getFlowId()));
                } else if (VIEW_METHOD.equals(pActionName))
                {
                    ClientFactory.goTo(new MethodCallDetailPlace(pRow.getFlowId(), pRow.getPosition()));
                }
            }
        });
        return tTable;
    }

    private ColumnDefinition<MethodCallSearchExtractDTO> getColumnDefinition()
    {
        ColumnDefinition<MethodCallSearchExtractDTO> cf = new ColumnDefinition<MethodCallSearchExtractDTO>();
        cf.addColumn(new ColumnFlowIdAction("FlowId", VIEW_FLOW, new Image(ClientFactory.imageBundle.edit()), null,
                                            "flow.id"));
        cf.addColumn(new ColumnMethodPositionAction("Position", VIEW_METHOD,
                                                    new Image(ClientFactory.imageBundle.edit()), null,
                                                    "methId.position"));

        cf.addColumn(new ColumnString<MethodCallSearchExtractDTO>("Thread", "flow.threadName", true)
        {
            @Override
            public String getStringValue(MethodCallSearchExtractDTO pValue)
            {
                return "<i>" + pValue.getFlowThread() + "</i>";
            }
        });
        cf.addColumn(new ColumnString<MethodCallSearchExtractDTO>("Server", "flow.jvmIdentifier", true)
        {
            @Override
            public String getStringValue(MethodCallSearchExtractDTO pValue)
            {
                return pValue.getFlowServer();
            }
        });
        cf.addColumn(new ColumnString<MethodCallSearchExtractDTO>("Duration", "flow.duration", true)
        {
            @Override
            public String getStringValue(MethodCallSearchExtractDTO pValue)
            {
                return String.valueOf(pValue.getFlowDuration());
            }
        });
        cf.addColumn(new ColumnString<MethodCallSearchExtractDTO>("Begin", "beginTime", true)
        {
            @Override
            public String getStringValue(MethodCallSearchExtractDTO pValue)
            {
                return pValue.getFlowBeginDate();
            }
        });
        // TODO Add ordering on duration
        cf.addColumn(new ColumnString<MethodCallSearchExtractDTO>("Method call duration", null, false)
        {
            @Override
            public String getStringValue(MethodCallSearchExtractDTO pValue)
            {
                return pValue.getDuration();
            }
        });
        cf.addColumn(new ColumnString<MethodCallSearchExtractDTO>("Group", "groupName", true)
        {
            @Override
            public String getStringValue(MethodCallSearchExtractDTO pValue)
            {
                return pValue.getGroup();
            }
        });
        cf.addColumn(new ColumnString<MethodCallSearchExtractDTO>("Class", "className", true)
        {
            @Override
            public String getStringValue(MethodCallSearchExtractDTO pValue)
            {
                return pValue.getClassName();
            }
        });
        cf.addColumn(new ColumnString<MethodCallSearchExtractDTO>("Method", "methodName", true)
        {
            @Override
            public String getStringValue(MethodCallSearchExtractDTO pValue)
            {
                return pValue.getMethodName();
            }
        });
        cf.addColumn(new ColumnString<MethodCallSearchExtractDTO>("Thrown Exception", "", false)
        {
            @Override
            public String getStringValue(MethodCallSearchExtractDTO pValue)
            {
                return pValue.getHasException();
            }
        });
        return cf;
    }

    public void setSearchCriterion(MethodCallSearchCriterion pSearchCriterion)
    {
        searchCriterion = pSearchCriterion;
    }
}
