package lib.util;

public class CTable implements java.io.Serializable
{
 private Object[][] Data=null;
 private int RowsQ=0;
public CTable(int _ColsQ)
{
	Data=new Object[_ColsQ][10];
}
public void addElements(Object[] _O)
{
	addRow();
	for (int n=0; n<Data.length; n++) Data[n][RowsQ-1]=_O[n];
}
public void addRow()
{
	RowsQ++;
	if (RowsQ>Data[0].length) makeData(2*Data[0].length);
}
public Object clone()
{
	CTable table=new CTable(getColsQ());
	for(int i=0; i<getRowsQ(); i++)
	{
	  table.addRow();
	  Object[] buf=new Object[getColsQ()];
	  System.arraycopy(getRow(i),0,buf,0,getColsQ());
	  table.setElements(buf,i);
	}	
	return table;
}
public Object element(Object _O, int _KeyCol, int _ValueCol)
{
 Object[] oa=null;
 int n=0;

	if (_KeyCol>=Data.length || _ValueCol>=Data.length) return null;

	n=indexOf(_O, _KeyCol);
	if (n>=0) return Data[_ValueCol][n];
	else return null;
}
public Object[] elements(Object _O, int _KeyCol)
{
 Object[] oa=null;
 int n=0;

	if (_KeyCol>=Data.length-1) return null;

	n=indexOf(_O, _KeyCol);
	if (n<0) return null;
	
	oa=new Object[Data.length];
	for (int k=0; k<Data.length; k++) oa[k]=Data[k][n];

	return oa;
}
public Object[] getCol(int _Col)
{
	if (_Col<0 || _Col>=Data.length) return null;

	return Data[_Col];
}
public int getColsQ()
{
	return Data.length;
}
public Object getElement(int _Col, int _Row)
{
	if (_Row<0 || _Row>=RowsQ || _Col<0 || _Col>=Data.length) return null;

	return Data[_Col][_Row];
}
public Object[] getRow(int _Row)
{
 Object[] oa=null;
 
	if (_Row<0 || _Row>=RowsQ) return null;

	oa=new Object[Data.length];
	for (int n=0; n<Data.length; n++) oa[n]=Data[n][_Row];

	return oa;
}
public int getRowsCapacity()
{
	return Data[0].length;
}
public int getRowsQ()
{
	return RowsQ;
}
public int indexOf(Object _O, int _KeyCol)
{
	for (int n=0; n<Data[_KeyCol].length; n++)
		if (Data[_KeyCol][n]!=null && Data[_KeyCol][n].equals(_O)) return n;

	return -1;
}
private void makeData(int _RowsQ)
{
 Object[][] oldData=null;
 int len=0;

	oldData=Data;
	Data=new Object[oldData.length][_RowsQ];
	len=oldData[0].length<Data[0].length?oldData[0].length:Data[0].length;
	for (int n=0; n<oldData.length; n++)
		System.arraycopy(oldData[n], 0, Data[n], 0, len);

}
public void removeAllRows()
{
	if (RowsQ==0) return;

	Data=new Object[Data.length][10];
	RowsQ=0;
}
public void removeRow(int _Row)
{
	if (_Row>=RowsQ) return;

	for (int n=0; n<Data.length; n++)
		System.arraycopy(Data[n], _Row+1, Data[n], _Row, RowsQ-_Row-1);

	RowsQ--;
}
public void setElement(Object _O, int _Col, int _Row)
{
	Data[_Col][_Row]=_O;
}
public void setElements(Object[] _O, int _Row)
{
	for (int n=0; n<Data.length; n++) Data[n][_Row]=_O[n];
}
public String toString()
{
 String str="";

	for (int k=0; k<RowsQ; k++)
	{
		str+="["+k+"] : ";
		for (int n=0; n<Data.length; n++)
		{
			str+=Data[n][k];
			if (n<Data.length-1) str+="~~";
		}
		if (k<RowsQ-1) str+='\n';
	}
	
	return str;
}
public void trimToSize()
{
	if (RowsQ<Data[0].length) makeData(RowsQ);
}
}