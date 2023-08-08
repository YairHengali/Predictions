package engine.action.impl;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.context.Context;
import engine.entity.EntityInstance;
import engine.property.api.PropertyInstance;
import engine.property.impl.DecimalProperty;
import engine.property.impl.FloatProperty;

import java.util.List;

public class Increase extends AbstractAction {
    //List<EntityInstance> mainEntityList;
    String propertyName;
    String byExpression; //TODO: Expression????

//    public Increase(List<EntityInstance> mainEntityList, String propertyName, float amountToIncrease){
//        this.mainEntityList = mainEntityList;
//
//        if (this.mainEntityList.getPropertyByName(propertyName).getValue() instanceof Number)//IN real TIME WILL BE OBJECT, PROBLEM
//        {
//            this.property = (Property<Number>) this.mainEntity.getPropertyByName(propertyName);
//        }
//        this.amountToIncrease = amountToIncrease; //TODO: NEED TO UNDERSTAND IN CASE OF VALUE THAT DEPENDS ENVIRONMENT
//    }


    public Increase(String mainEntityName, String propertyName, String byExpression) { //TODO: EXCEPTION IF property from non-Number type
        super(ActionType.INCREASE, mainEntityName);
        this.propertyName = propertyName;
        this.byExpression = byExpression; //TODO: NEED TO UNDERSTAND IN CASE OF VALUE THAT DEPENDS ENVIRONMENT
    }


    @Override
    public void Run(Context context) throws Exception {
//            PropertyInstance entityPropertyInstance = context.getPrimaryEntityInstance().getPropertyByName(propertyName);
//            if (entityPropertyInstance instanceof DecimalProperty)
//            {
//                ((DecimalProperty) entityPropertyInstance).setValue(((DecimalProperty) entityPropertyInstance).getValue() + byExpression.intValue());//TODO: VALIDATE IF INT OR FLOAT
//            }
//            else if (entityPropertyInstance instanceof FloatProperty)
//            {
//                ((FloatProperty) entityPropertyInstance).setValue(((FloatProperty) entityPropertyInstance).getValue() + amountToIncrease.floatValue());//TODO: VALIDATE IF INT OR FLOAT
//            } else {
//                throw new Exception("Invalid Property type, need to be Numeric");
//            }
//        }
    }
}
