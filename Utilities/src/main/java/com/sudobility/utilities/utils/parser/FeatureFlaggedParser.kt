package com.sudobility.utilities.utils.parser

import com.sudobility.utilities.utils.featureflags.FeatureService


public class FeatureFlaggedParser: ConditionalParser() {

    override fun conditioned(data: Any?) : Any? {
        var conditions = this.conditions?.toMutableMap() ?: mutableMapOf<String , String>()
        val features = FeatureService.shared?.featureFlags
        if (features != null) {
            for ((key, value) in features) {
                val string = Parser.standard.asString(value)
                string.let {
                    conditions[key] = string!!
                }
            }
        }
        this.conditions = conditions
        return super.conditioned(data)
    }
}


public val Parser.Companion.featureFlagged: Parser
    get() = FeatureFlaggedParser()