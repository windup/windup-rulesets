package org.jboss.windup.rules.tests;

import org.apache.commons.lang3.StringUtils;
import org.jboss.windup.config.GraphRewrite;
import org.jboss.windup.config.condition.GraphCondition;
import org.jboss.windup.config.query.Query;
import org.jboss.windup.config.query.QueryBuilderFind;
import org.jboss.windup.config.query.QueryGremlinCriterion;
import org.jboss.windup.config.query.QueryPropertyComparisonType;
import org.jboss.windup.graph.model.resource.FileModel;
import org.jboss.windup.reporting.model.InlineHintModel;
import org.ocpsoft.rewrite.context.EvaluationContext;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;

/**
 * Returns true if there are {@link InlineHintModel} entries that match the given message text.
 */
public class HintAssertion extends GraphCondition
{
    private String filename;
    private String messagePattern;

    private HintAssertion(String messagePattern)
    {
        this.messagePattern = messagePattern;
    }

    /**
     * Use the given message regular expression to match against {@link InlineHintModel#getHint()} property.
     */
    public static HintAssertion withMessage(String messagePattern)
    {
        return new HintAssertion(messagePattern);
    }

    /**
     * Only match {@link InlineHintModel}s that reference the given filename.
     */
    public HintAssertion in(String filename)
    {
        this.filename = filename;
        return this;
    }

    @Override
    public boolean evaluate(GraphRewrite event, EvaluationContext context)
    {
        QueryBuilderFind q = Query.fromType(InlineHintModel.class);
        if (StringUtils.isNotBlank(filename))
        {
            q.piped(new QueryGremlinCriterion()
            {
                private static final String HINT_STEP = "hintModel";

                @Override
                public void query(GraphRewrite event, GremlinPipeline<Vertex, Vertex> pipeline)
                {
                    pipeline.as(HINT_STEP);
                    pipeline.out(InlineHintModel.FILE_MODEL);
                    pipeline.has(FileModel.FILE_NAME, filename);
                    pipeline.back(HINT_STEP);
                }
            });
        }
        q.withProperty(InlineHintModel.HINT, QueryPropertyComparisonType.REGEX, messagePattern);
        return q.evaluate(event, context);
    }

}
