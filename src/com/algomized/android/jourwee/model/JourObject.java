package com.algomized.android.jourwee.model;

import com.google.api.client.util.Key;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class JourObject
{
  private final ArrayList<JourObjectChangeListener> mListeners = new ArrayList<JourObjectChangeListener>();

  public JourObject()
  {
  }

  public JourObject(JourObject paramJourObject)
  {
    this.mListeners.addAll(paramJourObject.mListeners);
  }

  private List<Field> getFieldsWithKeys(Class<? extends Object> paramClass)
  {
    ArrayList<Field> localArrayList = new ArrayList<Field>();
    Field[] arrayOfField = paramClass.getFields();
    
    for (int j =0; j < arrayOfField.length; j++)
    {
      Field localField = arrayOfField[j];
      if (localField.getAnnotation(Key.class) != null)
      {
        localArrayList.add(localField);
      }
    }
    return localArrayList;
  }

  private String getKeyName(Field paramField)
  {
    Key localKey = (Key)paramField.getAnnotation(Key.class);
    if ((localKey != null) && (localKey.value() != null))
      return localKey.value();
    return paramField.getName();
  }

  public void addChangeListener(JourObjectChangeListener paramJourObjectChangeListener)
  {
    this.mListeners.add(paramJourObjectChangeListener);
  }

  public boolean copy(JourObject paramJourObject)
  {
    if (paramJourObject != null)
    {
      int i = 0;
      try
      {
        Iterator<Field> localIterator = getFieldsWithKeys(getClass()).iterator();
        while (localIterator.hasNext())
        {
          Field localField = (Field)localIterator.next();
          Object localObject1 = localField.get(this);
          Object localObject2 = localField.get(paramJourObject);
          localField.set(this, localObject2);
          if ((localObject1 != null) || (localObject2 != null))
            if (localObject1 != null)
            {
              boolean bool = localObject1.equals(localObject2);
              if (bool);
            }
            else
            {
              i |= 1;
            }
        }
        if (i == 0)
          return true;
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
    return false;
  }

  public boolean equals(Object paramObject)
  {
    if (paramObject instanceof JourObject)
    {
      try
      {
        Iterator<Field> localIterator = getFieldsWithKeys(getClass()).iterator();
        while (true)
          if (localIterator.hasNext())
          {
            Field localField = (Field)localIterator.next();
            Object localObject1 = localField.get(this);
            Object localObject2 = localField.get(paramObject);
            if ((localObject1 != null) || (localObject2 != null))
            {
              if ((localObject1 == null) || (localObject2 == null))
                break;
              boolean bool = localObject1.equals(localObject2);
              if (!bool)
                return false;
            }
          }
      }
      catch (Exception localException)
      {
        return false;
      }
    }
    return true;
  }

  public void notifyChange()
  {
    Iterator<JourObjectChangeListener> localIterator = this.mListeners.iterator();
    while (localIterator.hasNext())
      ((JourObjectChangeListener)localIterator.next()).onObjectChange(this);
  }

  public void removeChangeListener(JourObjectChangeListener paramJourObjectChangeListener)
  {
    this.mListeners.remove(paramJourObjectChangeListener);
  }

  protected void update(JourObject paramJourObject)
  {
    if (!copy(paramJourObject))
      notifyChange();
  }

  public static abstract interface JourObjectChangeListener
  {
    public abstract void onObjectChange(JourObject paramJourObject);
  }
}
