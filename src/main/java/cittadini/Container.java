/*
    Limiti Luca 738873 (sede VA)
    Zehhaf Ishak 737763 (sede VA)
    Ferro Paolo 737529 (sede VA)
    Casolo Ginelli Loris 737056 (sede VA)
 */

package cittadini;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe per memorizzare oggetti di tipi differenti in un'unica struttura dati
 */
public class Container implements Serializable,Cloneable {
    private ArrayList<Object> _arr = new ArrayList<>();

    /**
     * Metodo per aggiungere un oggetto in un'ArrayList di Object
     * @param b oggetto da aggiungere
     */
    public void setObject(Object b)
    {
        _arr.add(b);
    }

    /**
     * Metodo per restituire un oggetto da un'ArrayList di Object ad un determinato indice
     * @param index indice della posizione dell'oggetto all'interno dell'ArrayList
     * @return un Object specifico presente all'indice passato come parametro
     */
    public Object getObject(int index)
    {
        return _arr.get(index);
    }

    /**
     * Metodo per restituire una String che rappresenta un oggetto da un'ArrayList di Object ad un determinato indice
     * @param index indice della posizione dell'oggetto all'interno dell'ArrayList
     * @return una String che rappresenta l'oggetto presente all'indice passato come parametro
     */
    public String getString(int index) {
        try {
            return getObject(index).toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Metodo per restituire un intero che rappresenta un oggetto da un'ArrayList di Object ad un determinato indice
     * @param index indice della posizione dell'oggetto all'interno dell'ArrayList
     * @return un intero che rappresenta l'oggetto presente all'indice passato come parametro
     */
    public int getInt(int index) {
        try {
            return (Integer) getObject(index);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Metodo per restituire una String che rappresenta un oggetto da un'ArrayList di Object ad un determinato indice
     * @param index indice della posizione dell'oggetto all'interno dell'ArrayList
     * @return una variabile booleana che rappresenta l'oggetto presente all'indice passato come parametro
     */
    public boolean getBool(int index) {
        try {
            return (Boolean) getObject(index);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Metodo per creare un oggetto Container identico ad un altro che ha istanziato questo metodo
     * @return un oggetto Container identico a quello che ha istanziato questo metodo
     */
    public Container Clone() {
        Container c = new Container();
        c._arr = new ArrayList<>(this._arr);
        return c;
    }
}
