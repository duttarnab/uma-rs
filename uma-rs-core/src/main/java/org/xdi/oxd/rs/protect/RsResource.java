package org.xdi.oxd.rs.protect;

import com.google.common.collect.Maps;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.xdi.oxauth.model.uma.JsonLogicNode;
import org.xdi.oxauth.model.uma.JsonLogicNodeParser;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 24/12/2015
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RsResource implements Serializable {

    @JsonProperty(value = "path")
    String path;
    @JsonProperty(value = "conditions")
    List<Condition> conditions;

    private Map<String, Condition> httpMethodToCondition = null;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public List<String> scopes(String httpMethod) {
        return getConditionMap().get(httpMethod).getScopes();
    }

    public List<String> getScopesForTicket(String httpMethod) {
        Condition condition = getConditionMap().get(httpMethod);
        final JsonLogicNode node = JsonLogicNodeParser.parseNode(condition.getScopeExpression().toString());
        if (node != null) {
            return node.getData(); // return all scopes defined in "data" of json object
        }
        return condition.getTicketScopes() != null && !condition.getTicketScopes().isEmpty() ?
                condition.getTicketScopes() : condition.getScopes();
    }

    private Map<String, Condition> getConditionMap() {
        if (httpMethodToCondition == null) {
            initMap();
        }
        return httpMethodToCondition;
    }

    private void initMap() {
        httpMethodToCondition = Maps.newHashMap();
        if (conditions != null) {
            for (Condition condition : conditions) {
                if (condition.getHttpMethods() != null) {
                    for (String httpMethod : condition.getHttpMethods()) {
                        httpMethodToCondition.put(httpMethod, condition);
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("RsResource");
        sb.append("{path='").append(path).append('\'');
        sb.append(", conditions=").append(conditions);
        sb.append(", httpMethodToCondition=").append(httpMethodToCondition);
        sb.append('}');
        return sb.toString();
    }
}
