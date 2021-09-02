package cittadini;

import java.io.Serializable;
import java.util.ArrayList;

public class Container implements Serializable,Cloneable
{
    private ArrayList<Object> _arr = new ArrayList<>();

    public void setObject(Object b)
    {
        _arr.add(b);
    }
    public Object getObject(int index)
    {
        return _arr.get(index);
    }

    public String getString(int index)
    {
        try
        {
            return getObject(index).toString();
        }
        catch (Exception e)
        {
            return "";
        }
    }
    public int getInt(int index)
    {
        try
        {
            return (Integer) getObject(index);
        }
        catch (Exception e)
        {
            return -1;
        }
    }
    public boolean getBool(int index)
    {
        try
        {
            return (Boolean) getObject(index);
        }
        catch (Exception e)
        {
            return false;
        }
    }
    public Container Clone()
    {
        Container c = new Container();
        c._arr = new ArrayList<>(this._arr);
        return c;
    }
}
