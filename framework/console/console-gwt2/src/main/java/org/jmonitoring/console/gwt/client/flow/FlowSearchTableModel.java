package org.jmonitoring.console.gwt.client.flow;

import it.pianetatecno.gwt.utility.client.table.Callback;
import it.pianetatecno.gwt.utility.client.table.Column;
import it.pianetatecno.gwt.utility.client.table.ColumnDefinition;
import it.pianetatecno.gwt.utility.client.table.PagingTable;
import it.pianetatecno.gwt.utility.client.table.Request;
import it.pianetatecno.gwt.utility.client.table.SerializableResponse;
import it.pianetatecno.gwt.utility.client.table.TableActions;
import it.pianetatecno.gwt.utility.client.table.TableModel;

import org.jmonitoring.console.gwt.client.flow.FlowSearch.RequestDTO;
import org.jmonitoring.console.gwt.client.main.JMonitoringAsyncCallBack;
import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;

public class FlowSearchTableModel extends TableModel<FlowExtractDTO>
{
    private RequestDTO driver;

    private FlowServiceAsync service;

    public FlowSearchTableModel(RequestDTO pDriver, FlowServiceAsync pService)
    {
        super();
        driver = pDriver;
        service = pService;
    }

    @Override
    public void requestRows(final Request pRequest, final Callback<FlowExtractDTO> pCallback)
    {
        // FlowSearchRequestDTO tDto = driver.flush();

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
            new PagingTable<FlowExtractDTO>(this, getColumnDefinition(), tTableActions, 10, "defaultSortingColumn",
                                            Column.SORTING_DESC);
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
            public String getValue(FlowExtractDTO valore)
            {
                return valore.getThread();
            }
        });
        cf.addColumn(new Column<String, FlowExtractDTO>("Thread", "Thread", true)
        {
            @Override
            public String getValue(FlowExtractDTO valore)
            {
                return valore.getThread();
            }
        });
        return cf;
    }

}
