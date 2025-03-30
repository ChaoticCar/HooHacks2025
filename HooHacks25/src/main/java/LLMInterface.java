import java.util.ArrayList;

public class LLMInterface {

    LLMAPIConnection llmAPIConnection;

    LLMInterface(LLMAPIConnection llmAPIConnection) {
        this.llmAPIConnection = llmAPIConnection;
    }

    public void startCombatAction(CombatState combatState, CombatEntity initiator,
                                  ArrayList<CombatEntity> targets, ActionType action, Item item, int D20) {
        String description = getCombatDescription(combatState, initiator, targets, action, item);
        System.out.println("Description: " + description);

        llmAPIConnection.executeCombatResult(description, initiator, targets, D20);

    }

    private String getCombatDescription(CombatState combatState, CombatEntity initiator,
                                        ArrayList<CombatEntity> targets, ActionType action, Item item) {
        StringBuilder description = new StringBuilder();

        switch (action) {
            case ATTACK: {
                String targetsName = "";
                if (targets.size() == 1) {
                    targetsName = targets.get(0).getName();
                } else if (targets.size() == 2) {
                    targetsName = targets.get(0).getName() + " and " + targets.get(1).getName();
                } else {
                    for (int i = 0; i < targets.size(); i++) {
                        if (i == targets.size() - 1) {
                            targetsName += "and " + targets.get(i).getName();
                        } else {
                            targetsName += targets.get(i).getName() + ", ";
                        }
                    }
                }
                description.append(initiator.getName())
                       .append("(the initiator) decided to attack ")
                       .append(targetsName);
                if (item != null) {
                    description.append(" using a ").append(getItemName(item));
                }
                description.append(".");
                break;
            }
            case ITEM: {
                description.append(initiator.getName())
                       .append(" used a ")
                       .append(getItemName(item));
                if (!targets.isEmpty()) {
                    String targetsName = "";
                    if (targets.size() == 1) {
                        targetsName = targets.get(0).getName() + " (the 1 target)";
                    } else if (targets.size() == 2) {
                        targetsName = targets.get(0).getName() + " and " + targets.get(1).getName() + " (the 2 targets)";
                    } else {
                        for (int i = 0; i < targets.size(); i++) {
                            if (i == targets.size() - 1) {
                                targetsName += "and " + targets.get(i).getName() + " (the " + targets.size() + " targets)";
                            } else {
                                targetsName += targets.get(i).getName() + ", ";
                            }
                        }
                    }
                    description.append(" on ").append(targetsName);
                }
                description.append(".");
                break;
            }
            case RUN: {
                description.append(initiator.getName())
                        .append(" decided to run away from combat.");
                break;
            }
            default: {
                description.append("Unknown action performed by ")
                        .append(initiator.getName())
                        .append(".");
                break;
            }
        }
        System.out.println(description);
        return description.toString();
    }

    private String getActionName (ActionType actionType) {
        switch (actionType) {
            case ATTACK: {
                return "attacked";
            }
            case ITEM: {
                return "used";
            }
            case RUN: {
                return "ran away";
            } default: {
                System.out.println("action hasn't been described yet: " + actionType);
                return "";
            }
        }
    }

    private String getItemName (Item item) {
        switch (item) {
            case SWORD: {
                return "sword";
            } default: {
                System.out.println("item hasn't been described yet: " + item);
                return "item";
            }
        }
    }
}
