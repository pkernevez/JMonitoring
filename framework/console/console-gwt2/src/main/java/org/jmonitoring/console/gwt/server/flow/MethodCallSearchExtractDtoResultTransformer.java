package org.jmonitoring.console.gwt.server.flow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.transform.ResultTransformer;
import org.jmonitoring.console.gwt.shared.method.MethodCallSearchExtractDTO;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.domain.MethodCallPO;

public class MethodCallSearchExtractDtoResultTransformer implements ResultTransformer
{

    private ConsoleDao dao;
private FormaterBean formater;
    
    private static final long serialVersionUID = 8188013809930886849L;

    public MethodCallSearchExtractDtoResultTransformer(ConsoleDao pDao, FormaterBean pFormater)
    {
        dao = pDao;
        formater=pFormater;
    }

    public Object transformTuple(Object[] pTuple, String[] pAliases)
    {
        Map<String, Object> tMap = new HashMap<String, Object>();
        for (int i = 0; i < pAliases.length; i++)
        {
            tMap.put(pAliases[i], pTuple[i]);
        }
        int tFlowId = (Integer) tMap.get("flowid");
        int tPosition = (Integer) tMap.get("position");

        MethodCallPO tMeth = dao.loadMethodCall(tFlowId, tPosition);
        MethodCallSearchExtractDTO tResult = new MethodCallSearchExtractDTO();
         tResult.setFlowId(String.valueOf(tMeth.getFlow().getId()));
         tResult.setFlowBeginDate(formater.formatDateTime(tMeth.getFlow().getBeginTime()));
         tResult.setFlowServer(tMeth.getFlow().getJvmIdentifier());
         tResult.setFlowDuration(String.valueOf(tMeth.getFlow().getDuration()));
         tResult.setFlowThread(tMeth.getFlow().getThreadName());
         tResult.setPosition(String.valueOf(tMeth.getPosition()));
         tResult.setDuration(String.valueOf(tMeth.getDuration()));
         tResult.setClassName(tMeth.getClassName());
         tResult.setMethodName(tMeth.getMethodName());
         tResult.setGroup(tMeth.getGroupName());
         tResult.setHasException(tMeth.getThrowableClass() == null ? "" : "yes");

        return tResult;
    }

    @SuppressWarnings("rawtypes")
    public List transformList(List pCollection)
    {
        return pCollection;
    }

}
