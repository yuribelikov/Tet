package lib.gcl;

import java.awt.*;

public class CTabsPanel extends CTabsPanelCanvas
{
  public CTabsPanel(Container _Owner, String[] _Caption, String[] _Tip)
  {
    int n = 0, q = 0;

    try
    {
      Owner = _Owner;
      setLayout(null);

      q = _Caption.length;
      Tab = new CTab[q];
      Page = new CPage[q];
      for (n = 0; n < q; n++)
      {
        Tab[n] = new CTab(this, _Caption[n], _Tip[n]);
        Tab[n].N = n;
        add(Tab[n]);
        Page[n] = new CPage(this);
        Page[n].setLayout(null);
        Page[n].N = n;
        add(Page[n]);
      }
      Tab[0].Selected = true;

    }
    catch (Exception exc)
    {
    }

  }
}