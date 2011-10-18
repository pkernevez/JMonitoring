package org.jmonitoring.console.gwt.client.flow;

import it.pianetatecno.gwt.utility.client.table.ActionHandler;
import it.pianetatecno.gwt.utility.client.table.Callback;
import it.pianetatecno.gwt.utility.client.table.Column;
import it.pianetatecno.gwt.utility.client.table.ColumnAction;
import it.pianetatecno.gwt.utility.client.table.ColumnDefinition;
import it.pianetatecno.gwt.utility.client.table.ColumnString;
import it.pianetatecno.gwt.utility.client.table.PagingTable;
import it.pianetatecno.gwt.utility.client.table.Request;
import it.pianetatecno.gwt.utility.client.table.SerializableResponse;
import it.pianetatecno.gwt.utility.client.table.TableModel;

import java.util.ArrayList;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.main.JMonitoringAsyncCallBack;
import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;

import com.google.gwt.user.client.ui.Image;

public class FlowSearchTableModel extends TableModel<FlowExtractDTO>
{
    private static final String VIEW_FLOW = "viewFlow";

    private final FlowServiceAsync service;

    /**
     * Workaround to no do a first call during the creation of the Tqble as will do that just after the filter
     * initialization. This is necessary to handle history.
     */
    private boolean firstTime = true;

    public FlowSearchTableModel(FlowServiceAsync pService)
    {
        super();
        service = pService;
    }

    @Override
    public void requestRows(final Request pRequest, final Callback<FlowExtractDTO> pCallback)
    {
        if (firstTime)
        {
            pCallback.onRowsReady(pRequest, createResponse());
            firstTime = false;
        } else
        {
            service.search(pRequest, new JMonitoringAsyncCallBack<SerializableResponse<FlowExtractDTO>>()
            {
                public void onSuccess(SerializableResponse<FlowExtractDTO> pResult)
                {
                    pCallback.onRowsReady(pRequest, pResult);
                }

            });
        }
    }

    private SerializableResponse<FlowExtractDTO> createResponse()
    {
        SerializableResponse<FlowExtractDTO> tResult = new SerializableResponse<FlowExtractDTO>();
        tResult.setRows(new ArrayList<FlowExtractDTO>());
        return tResult;
    }

    public PagingTable<FlowExtractDTO> getTable()
    {
        // Add the actions on the table
        PagingTable<FlowExtractDTO> tTable =
            new PagingTable<FlowExtractDTO>(this, getColumnDefinition(), 10, "id", Column.SORTING_ASC);
        tTable.addActionHandler(new ActionHandler<FlowExtractDTO>()
        {
            public void onActionPerformed(String actionName, FlowExtractDTO pRow)
            {
                if (VIEW_FLOW.equals(actionName))
                {
                    ClientFactory.getPlaceController().goTo(new FlowDetailPlace(pRow.getId()));
                }
            }
        });
        return tTable;
    }

    private ColumnDefinition<FlowExtractDTO> getColumnDefinition()
    {
        ColumnDefinition<FlowExtractDTO> cf = new ColumnDefinition<FlowExtractDTO>();
        cf.addColumn(new ColumnAction<FlowExtractDTO>(VIEW_FLOW, new Image(ClientFactory.imageBundle.edit()), null));

        cf.addColumn(new ColumnString<FlowExtractDTO>("Id", "id", true)
        {
            @Override
            public String getStringValue(FlowExtractDTO pValue)
            {
                return String.valueOf(pValue.getId());
            }
        });
        cf.addColumn(new ColumnString<FlowExtractDTO>("Thread", "thread", true)
        {
            @Override
            public String getStringValue(FlowExtractDTO pValue)
            {
                return "<i>" + pValue.getThreadName() + "</i>";
            }
        });
        cf.addColumn(new ColumnString<FlowExtractDTO>("Server", "jvmIdentifier", true)
        {
            @Override
            public String getStringValue(FlowExtractDTO pValue)
            {
                return pValue.getServer();
            }
        });
        cf.addColumn(new ColumnString<FlowExtractDTO>("Duration", "duration", true)
        {
            @Override
            public String getStringValue(FlowExtractDTO pValue)
            {
                return String.valueOf(pValue.getDuration());
            }
        });
        cf.addColumn(new ColumnString<FlowExtractDTO>("Begin", "beginTime", true)
        {
            @Override
            public String getStringValue(FlowExtractDTO pValue)
            {
                return pValue.getBeginTime();
            }
        });
        cf.addColumn(new ColumnString<FlowExtractDTO>("End", "endTime", true)
        {
            @Override
            public String getStringValue(FlowExtractDTO pValue)
            {
                return pValue.getEndTime();
            }
        });
        cf.addColumn(new ColumnString<FlowExtractDTO>("Class", "firstClassName", true)
        {
            @Override
            public String getStringValue(FlowExtractDTO pValue)
            {
                return pValue.getClassName();
            }
        });
        cf.addColumn(new ColumnString<FlowExtractDTO>("Method", "firstMethodName", true)
        {
            @Override
            public String getStringValue(FlowExtractDTO pValue)
            {
                return pValue.getMethodName();
            }
        });
        return cf;
    }
}
