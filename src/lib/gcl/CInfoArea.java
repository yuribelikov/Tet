package lib.gcl;

import java.awt.*;

public class CInfoArea extends Canvas
{
  public static final int LEFT = 0;
  public static final int RIGHT = 1;
  public static final int CENTER = 2;

  private Container Owner = null;
  public int Align = LEFT;
  private String Text = "";
  private boolean WordWrap = false;

  public CInfoArea(Container _Owner)
  {
    this(_Owner, "");
  }

  public CInfoArea(Container _Owner, String _Text)
  {
    Owner = _Owner;
    Text = _Text;
    setBackground(Owner.getBackground());
  }

  // count _C in _Str
  public static int count(String _Str, char _C)
  {
    int pos1 = 0, pos2 = 0, len = 0, n = 0;

    if (_Str == null) return 0;

    pos1 = pos2 = 0;
    len = _Str.length();
    while (n < len)
    {
      pos2 = _Str.indexOf(_C, pos1);
      if (pos2 < 0 || pos2 >= len) break;
      n++;
      pos1 = pos2 + 1;
    }

    return n;
  }

  // create String[] from _Str ("aaa"+_C+"bbb"+...)
  public static String[] explode(String _Str, char _C, boolean _Trim)
  {
    String str = "", s0 = "";
    int pos1 = 0, pos2 = 0, len = 0, n = 0;
    String[] sa = null;

    if (_Str == null) return null;

    sa = new String[count(_Str, _C) + 1];
    sa[0] = "";
    pos1 = pos2 = 0;
    len = _Str.length();
    while (n < len)
    {
      pos2 = _Str.indexOf(_C, pos1);
      if (pos2 < 0 || pos2 > len) pos2 = len;
      s0 = _Str.substring(pos1, pos2);
      if (s0 == null) s0 = "";
      if (_Trim) sa[n] = s0.trim();
      else sa[n] = s0;
      if (pos2 == len) break;
      n++;
      pos1 = pos2 + 1;
    }

    return sa;
  }

  public int getLinesCount()
  {
    int w = 0, h = 0, x = 0, y = 0, sw = 0, ch = 0, n = 0, q = 0, k = 0, bw = 0, lq = 0;
    int d = 3;
    String[] sa = null, ssa = null;
    FontMetrics fm = null;
    String str = "";

    try
    {
      w = getSize().width;
      h = getSize().height;

      sa = explode(Text, '\n', true);
      q = sa.length;
      fm = getFontMetrics(getFont());
      bw = fm.stringWidth(" ");
      ch = getFontMetrics(getFont()).getHeight();
      for (n = 0; n < q; n++)
      {
        x = 0;
        y += ch;
        lq++;
        if (WordWrap)          // wrap mode is on
        {
          ssa = explode(sa[n], ' ', true);  // separate string by words
          if (Align == LEFT)
          {
            for (k = 0; k < ssa.length; k++)
            {
              if (ssa[k].length() < 1) continue;
              sw = fm.stringWidth(ssa[k]);
              if (x + sw > w && x > 0)
              { // try wrap long word to another row
                x = 0;
                y += ch;
                lq++;
              }
              x += sw + bw;
            }
          }
          if (Align == RIGHT)
          {
            str = "";
            for (k = 0; k < ssa.length; k++)
            {
              if (ssa[k].length() < 1) continue;
              sw = fm.stringWidth(ssa[k]);
              if (x + sw > w && x > 0)
              { // try wrap long word to another row
                str = "";
                x = 0;
                y += ch;
                lq++;
              }
              if (str.length() > 0) str += " ";
              str += ssa[k];
              x += sw + bw;
            }
          }
          if (Align == CENTER)
          {
            str = "";
            for (k = 0; k < ssa.length; k++)
            {
              if (ssa[k].length() < 1) continue;
              sw = fm.stringWidth(ssa[k]);
              if (x + sw > w && x > 0)
              { // try wrap long word to another row
                str = "";
                x = 0;
                y += ch;
                lq++;
              }
              if (str.length() > 0) str += " ";
              str += ssa[k];
              x += sw + bw;
            }
          }
        }              // wrap mode is off
      }

    }
    catch (Exception e1)
    {
    }

    return lq;
  }

  public String getText()
  {
    return Text;
  }

  public void paint()
  {
    paint(getGraphics());
  }

  public void paint(Graphics g)
  {
    int w = 0, h = 0, x = 0, y = 0, sw = 0, ch = 0, n = 0, q = 0, k = 0, bw = 0;
    int d = 3;
    String[] sa = null, ssa = null;
    FontMetrics fm = null;
    String str = "";

    try
    {
      w = getSize().width;
      h = getSize().height;
      g.setClip(0, 0, w, h);
      super.paint(g);
      g.setColor(getForeground());

      sa = explode(Text, '\n', true);
      q = sa.length;
      fm = g.getFontMetrics(getFont());
      bw = fm.stringWidth(" ");
      ch = g.getFontMetrics(getFont()).getHeight();
      for (n = 0; n < q; n++)
      {
        x = 0;
        y += ch;
        if (WordWrap)          // wrap mode is on
        {
          ssa = explode(sa[n], ' ', true);  // separate string by words
          if (Align == LEFT)
          {
            for (k = 0; k < ssa.length; k++)
            {
              if (ssa[k].length() < 1) continue;
              sw = fm.stringWidth(ssa[k]);
              if (x + sw > w && x > 0)
              { // try wrap long word to another row
                x = 0;
                y += ch;
              }
              g.drawString(ssa[k], x + d, y);
              x += sw + bw;
            }
          }
          if (Align == RIGHT)
          {
            str = "";
            for (k = 0; k < ssa.length; k++)
            {
              if (ssa[k].length() < 1) continue;
              sw = fm.stringWidth(ssa[k]);
              if (x + sw > w && x > 0)
              { // try wrap long word to another row
                g.drawString(str, w - fm.stringWidth(str) + d, y);
                str = "";
                x = 0;
                y += ch;
              }
              if (str.length() > 0) str += " ";
              str += ssa[k];
              x += sw + bw;
            }
            g.drawString(str, w - fm.stringWidth(str) + d, y);
          }
          if (Align == CENTER)
          {
            str = "";
            for (k = 0; k < ssa.length; k++)
            {
              if (ssa[k].length() < 1) continue;
              sw = fm.stringWidth(ssa[k]);
              if (x + sw > w && x > 0)
              { // try wrap long word to another row
                g.drawString(str, (w - fm.stringWidth(str)) / 2 + d, y);
                str = "";
                x = 0;
                y += ch;
              }
              if (str.length() > 0) str += " ";
              str += ssa[k];
              x += sw + bw;
            }
            g.drawString(str, (w - fm.stringWidth(str)) / 2 + d, y);
          }
        }              // wrap mode is off
        else g.drawString(sa[n], x, y);
      }

    }
    catch (Exception e1)
    {
    }

  }

  public void setAlign(int _Align)
  {
    if (_Align < LEFT || _Align > CENTER) return;

    Align = _Align;
    paint();
  }

  public void setForeground(Color _C)
  {
    super.setForeground(_C);
    paint();
  }

  public void setText(String _Text)
  {
    Text = _Text;
    paint();
  }

  public void setWordWrap(boolean _F)
  {
    WordWrap = _F;
    paint();
  }
}