package org.jmonitoring.console.gwt.client.flow;

import it.pianetatecno.gwt.utility.client.table.Callback;
import it.pianetatecno.gwt.utility.client.table.Column;
import it.pianetatecno.gwt.utility.client.table.ColumnDefinition;
import it.pianetatecno.gwt.utility.client.table.Filter;
import it.pianetatecno.gwt.utility.client.table.PagingTable;
import it.pianetatecno.gwt.utility.client.table.Request;
import it.pianetatecno.gwt.utility.client.table.SerializableResponse;
import it.pianetatecno.gwt.utility.client.table.TableActions;
import it.pianetatecno.gwt.utility.client.table.TableModel;

import java.util.List;

import org.jmonitoring.console.gwt.client.main.JMonitoringAsyncCallBack;
import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;

public class FlowSearchTableModel extends TableModel<FlowExtractDTO>
{
    private FlowServiceAsync service;
    
    public FlowSearchTableModel(  FlowServiceAsync pService)
    {
        super();
        service = pService;
    }

    @Override
    public void requestRows(final Request pRequest, final Callback<FlowExtractDTO> pCallback)
    {
        service.search(pRequest, new JMonitoringAsyncCallBack<SerializableResponse<FlowExtractDTO>>()
        {
            public void onSuccess(SerializableResponse<FlowExtractDTO> pResult)
            {
                pCallback.onRowsReady(pRequest, pResult);
            }

        });

    }

    public PagingTable<FlowExtractDTO> getTable()
    {
        // Add the actions on the table
        TableActions tTableActions = new TableActions();
        // tableActions.addAction("Edit", MainEntryPoint.immaginiApp.iconCloseSmall().getHTML());
        PagingTable<FlowExtractDTO> tTable =
            new PagingTable<FlowExtractDTO>(this, getColumnDefinition(), tTableActions, 10, "id",
                                            Column.SORTING_ASC);
        // table.addActionHandler(new ActionHandler<Operatore>() {
        // @Override
        // public void onActionPerformed(String eventName, Operatore object) {
        // Window.alert("Action "+eventName+" object "+object.getCognome()+" "+object.getNome());
        // }
        // });

        return tTable;
    }

    private ColumnDefinition getColumnDefinition()
    {
        ColumnDefinition cf = new ColumnDefinition();
        cf.addColumn(new Column<String, FlowExtractDTO>("Id", "Id", true)
        {
            @Override
            public String getValue(FlowExtractDTO pValue)
            {
                return String.valueOf(pValue.getId());
            }
        });
        cf.addColumn(new Column<String, FlowExtractDTO>("Thread", "Thread", true)
        {
            @Override
            public String getValue(FlowExtractDTO pValue)
            {
                return pValue.getThreadName();
            }
        });
        cf.addColumn(new Column<String, FlowExtractDTO>("Server", "Server", true)
        {
            @Override
            public String getValue(FlowExtractDTO pValue)
            {
                return pValue.getServer();
            }
        });
        cf.addColumn(new Column<String, FlowExtractDTO>("Duration", "Duration", true)
        {
            @Override
            public String getValue(FlowExtractDTO pValue)
            {
                return String.valueOf( pValue.getDuration());
            }
        });
        cf.addColumn(new Column<String, FlowExtractDTO>("Begin", "Begin", true)
        {
            @Override
            public String getValue(FlowExtractDTO pValue)
            {
                return pValue.getBeginTime();
            }
        });
        cf.addColumn(new Column<String, FlowExtractDTO>("End", "End", true)
        {
            @Override
            public String getValue(FlowExtractDTO pValue)
            {
                return pValue.getEndTime();
            }
        });
        cf.addColumn(new Column<String, FlowExtractDTO>("Class", "Class", true)
        {
            @Override
            public String getValue(FlowExtractDTO pValue)
            {
                return pValue.getClassName();
            }
        });
        cf.addColumn(new Column<String, FlowExtractDTO>("Method", "Method", true)
        {
            @Override
            public String getValue(FlowExtractDTO pValue)
            {
                return pValue.getMethodName();
            }
        });
       return cf;
    }

}
