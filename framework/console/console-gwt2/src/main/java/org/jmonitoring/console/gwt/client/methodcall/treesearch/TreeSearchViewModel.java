package org.jmonitoring.console.gwt.client.methodcall.treesearch;

import org.jmonitoring.console.gwt.shared.method.treesearch.ClassDTO;
import org.jmonitoring.console.gwt.shared.method.treesearch.HasName;
import org.jmonitoring.console.gwt.shared.method.treesearch.PackageDTO;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

public class TreeSearchViewModel implements TreeViewModel
{

    private static final NoSelectionModel<HasName> sSelectionModel = new NoSelectionModel<HasName>();

    /**
     * This class isn't very beautifull with a lot of cast. But it's the only way I found to have several type of Cell
     * at only one level. This is due to the limitation of the getNodeInfo(T value) method that accept only one Cell
     * class for all the children.
     * 
     * @see GWT JavaDoc http://code.google.com/intl/fr/webtoolkit/doc/latest/DevGuideUiCellWidgets.html#celltree
     * @see http://groups.google.com/group/google-web-toolkit/browse_thread/thread/fab6491dffb73874
     * @author pke
     * 
     */
    private static class UniversalCell extends AbstractCell<HasName>
    {
        public UniversalCell()
        {
            super();

        }

        @Override
        public void render(Context pContext, HasName pValue, SafeHtmlBuilder pSb)
        {
            if (pValue != null)
            {
                pSb.appendEscaped(pValue.getName());
            }
        }
    }

    public <T> NodeInfo<?> getNodeInfo(T pValue)
    {
        if (pValue == null)
        {
            return null;
        } else
        {
            if (pValue instanceof PackageDTO)
            {
                ListDataProvider<HasName> tSubData = new ListDataProvider<HasName>();
                PackageDTO tPackageDTO = (PackageDTO) pValue;
                tSubData.getList().addAll(tPackageDTO.getSubPackages());
                tSubData.getList().addAll(tPackageDTO.getClasses());
                return new DefaultNodeInfo<HasName>(tSubData, new UniversalCell(), sSelectionModel, null);
            } else if (pValue instanceof ClassDTO)
            {
                // return new DefaultNodeInfo<ClassDTO>(new ListDataProvider<ClassDTO>(tPackageDTO.getClasses()),
                // new ClassCell());
                return null;
            } else
            {
                throw new RuntimeException("Unexpected value : " + pValue);
            }
        }
    }

    public boolean isLeaf(Object pValue)
    {
        return (pValue == null || pValue instanceof ClassDTO);
    }

}
