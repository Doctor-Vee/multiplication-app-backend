with user_pricing as (SELECT pud.international_percentage                                           AS intlDiscountPercentage,
                             pud.local_percentage                                                   AS localDiscountPercentage,
                             pud.intl_range_amount_deduction_percentage                             AS intlRangeAmountDeductionPercentage,
                             pud.intl_fixed_amount_deduction_percentage                             AS intlFixedAmountDeductionPercentage,
                             pud.intl_amount_fee                                                    AS intlFeeAmount,
                             pud.intl_amount_fee_percentage                                         AS intlFeePercentage,
                             pud.local_amount_fee                                                   AS localFeeAmount,
                             pud.local_amount_fee_percentage                                        AS localFeePercentage,
                             pud.operator_transaction_status                                        AS operatorTransactionStatus,
                             pud.provider_to_use_for_denominations_display                          AS providerToUseForDenominationsDisplay,
                             pud.fx_rate                                                            AS userFxRate,
                             pud.fx_rate_override                                                   AS userFxRateOverride,
                             pud.fx_rate_local                                                      AS userFxRateLocal,
                             o.id                                                                   AS operatorId,
                             o.name                                                                 AS operatorName,
                             c.name                                                                 AS countryName,
                             po.id                                                                  AS providerOperatorId,
                             po.fx_rate_in_use                                                      AS fxRateInUse,
                             po.status                                                              AS providerOperatorStatus,
                             po.provider_requires_local_amount                                      AS providerRequiresLocalAmount,
                             po.fx_controlled_in_house                                              AS fxControlledInHouse,
                             po.provider_id                                                         AS providerId,
                             poa.id                                                                 AS providerOperatorAmountId,
                             poa.denomination_type                                                  AS denominationType,
                             poa.intl_min_amount                                                    AS intlMinAmount,
                             poa.intl_max_amount                                                    AS intlMaxAmount,
                             poa.local_min_amount                                                   AS localMinAmount,
                             poa.local_max_amount                                                   AS localMaxAmount,
                             cr.operator_id                                                         AS customRoutingOperatorId,
                             cr.allow_failover                                                      AS customRoutingAllowFailover,
                             cr.provider_id                                                         AS customRoutingProviderId,
                             cr.deleted_at                                                          AS customRoutingDeletedAt,
                             p.status                                                               AS customRoutingProviderStatus,
                             p.name                                                                 AS customRoutingProviderName,
                             GROUP_CONCAT(fta.international_amount)                                 AS fixedInternationalAmounts,
                             GROUP_CONCAT(fta.international_amount + fta.intl_amount_markup_amount) AS fixedInternationalAmountsWithMarkup,
                             GROUP_CONCAT(fta.local_amount + fta.intl_amount_markup_amount)         AS fixedLocalAmountsWithMarkup,
                             GROUP_CONCAT(fta.local_amount)                                         AS fixedLocalAmounts,
                             poa.local_amount_currency_code                                         AS localAmountCurrencyCode
                      FROM prepaid_user_discount pud
                               LEFT JOIN operator o ON pud.operator_id = o.id
                               LEFT JOIN country c ON o.country_id = c.id
                               LEFT JOIN provider_operator po ON o.id = po.operator_id
                               LEFT JOIN provider_operator_amount poa ON po.provider_operator_amount_id = poa.id
                               LEFT JOIN fixed_topup_amount fta ON poa.id = fta.provider_operator_amount_id
                               LEFT JOIN prepaid_account pa ON pud.prepaid_account_id = pa.id
                               LEFT JOIN custom_routing cr ON o.id = cr.operator_id AND cr.prepaid_account_id = pa.id
                               LEFT JOIN provider p ON cr.provider_id = p.id
                      WHERE pud.prepaid_account_id = :prepaidAccountId
                        AND po.status = 'ACTIVE'
                        AND o.deleted_at IS NULL
                        AND po.deleted_at IS NULL
                        AND (o.name LIKE :operatorName0)
                      GROUP BY o.id, o.name, c.name
                      ORDER BY c.name, o.name),
     default_pricing AS (SELECT o.id                                                                   AS operatorId,
                                o.name                                                                 AS operatorName,
                                c.name                                                                 AS countryName,
                                o.default_intl_discount_percentage                                     AS intlDiscountPercentage,
                                o.default_local_discount_percentage                                    AS localDiscountPercentage,
                                o.default_intl_range_amount_deduction_percentage                       AS intlRangeAmountDeductionPercentage,
                                o.default_intl_fixed_amount_deduction_percentage                       AS intlFixedAmountDeductionPercentage,
                                CAST(0 AS DECIMAL(10, 2))                                              AS intlFeeAmount,
                                CAST(0 AS DECIMAL(10, 2))                                              AS intlFeePercentage,
                                CAST(0 AS DECIMAL(10, 2))                                              AS localFeeAmount,
                                CAST(0 AS DECIMAL(10, 2))                                              AS localFeePercentage,
                                o.transaction_status                                                   AS operatorTransactionStatus,
                                NULL                                                                   AS providerToUseForDenominationsDisplay,
                                CAST(0 AS decimal(10, 5))                                              AS userFxRate,
                                CAST(0 AS decimal(10, 5))                                              AS userFxRateOverride,
                                CAST(0 AS decimal(10, 5))                                              AS userFxRateLocal,
                                po.id                                                                  AS providerOperatorId,
                                po.fx_rate_in_use                                                      AS fxRateInUse,
                                po.status                                                              AS providerOperatorStatus,
                                po.provider_requires_local_amount                                      AS providerRequiresLocalAmount,
                                po.fx_controlled_in_house                                              AS fxControlledInHouse,
                                po.provider_id                                                         AS providerId,
                                poa.id                                                                 AS providerOperatorAmountId,
                                poa.denomination_type                                                  AS denominationType,
                                poa.intl_min_amount                                                    AS intlMinAmount,
                                poa.intl_max_amount                                                    AS intlMaxAmount,
                                poa.local_min_amount                                                   AS localMinAmount,
                                poa.local_max_amount                                                   AS localMaxAmount,
                                cr.operator_id                                                         AS customRoutingOperatorId,
                                cr.allow_failover                                                      AS customRoutingAllowFailover,
                                cr.provider_id                                                         AS customRoutingProviderId,
                                cr.deleted_at                                                          AS customRoutingDeletedAt,
                                p.status                                                               AS customRoutingProviderStatus,
                                p.name                                                                 AS customRoutingProviderName,
                                GROUP_CONCAT(fta.international_amount)                                 AS fixedInternationalAmounts,
                                GROUP_CONCAT(fta.international_amount + fta.intl_amount_markup_amount) AS fixedInternationalAmountsWithMarkup,
                                GROUP_CONCAT(fta.local_amount + fta.intl_amount_markup_amount)         AS fixedLocalAmountsWithMarkup,
                                GROUP_CONCAT(fta.local_amount)                                         AS fixedLocalAmounts,
                                poa.local_amount_currency_code                                         AS localAmountCurrencyCode
                         FROM operator o
                                  LEFT JOIN country c ON o.country_id = c.id
                                  LEFT JOIN provider_operator po ON o.id = po.operator_id
                                  LEFT JOIN provider_operator_amount poa ON po.provider_operator_amount_id = poa.id
                                  LEFT JOIN fixed_topup_amount fta ON poa.id = fta.provider_operator_amount_id
                                  LEFT JOIN custom_routing cr
                                            ON o.id = cr.operator_id AND cr.prepaid_account_id = :prepaidAccountId
                                  LEFT JOIN provider p ON cr.provider_id = p.id
                         WHERE po.status = 'ACTIVE'
                           AND o.status = 'ACTIVE'
                           AND o.id NOT IN (SELECT operatorId from user_pricing)
                           AND o.deleted_at IS NULL
                           AND po.deleted_at IS NULL
                           AND (o.name LIKE :operatorName0)
                         GROUP BY o.id, o.name, c.name
                         ORDER BY c.name, o.name)
SELECT operatorId,
       operatorName,
       countryName,
       intlDiscountPercentage,
       localDiscountPercentage,
       intlRangeAmountDeductionPercentage,
       intlFixedAmountDeductionPercentage,
       intlFeeAmount,
       intlFeePercentage,
       localFeeAmount,
       localFeePercentage,
       operatorTransactionStatus,
       providerToUseForDenominationsDisplay,
       userFxRate,
       userFxRateOverride,
       userFxRateLocal,
       providerOperatorId,
       fxRateInUse,
       providerOperatorStatus,
       providerRequiresLocalAmount,
       fxControlledInHouse,
       providerId,
       providerOperatorAmountId,
       denominationType,
       intlMinAmount,
       intlMaxAmount,
       localMinAmount,
       localMaxAmount,
       customRoutingOperatorId,
       customRoutingAllowFailover,
       customRoutingProviderId,
       customRoutingDeletedAt,
       customRoutingProviderStatus,
       customRoutingProviderName,
       fixedInternationalAmounts,
       fixedInternationalAmountsWithMarkup,
       fixedLocalAmountsWithMarkup,
       fixedLocalAmounts,
       localAmountCurrencyCode
FROM user_pricing
UNION
SELECT operatorId,
       operatorName,
       countryName,
       intlDiscountPercentage,
       localDiscountPercentage,
       intlRangeAmountDeductionPercentage,
       intlFixedAmountDeductionPercentage,
       intlFeeAmount,
       intlFeePercentage,
       localFeeAmount,
       localFeePercentage,
       operatorTransactionStatus,
       providerToUseForDenominationsDisplay,
       userFxRate,
       userFxRateOverride,
       userFxRateLocal,
       providerOperatorId,
       fxRateInUse,
       providerOperatorStatus,
       providerRequiresLocalAmount,
       fxControlledInHouse,
       providerId,
       providerOperatorAmountId,
       denominationType,
       intlMinAmount,
       intlMaxAmount,
       localMinAmount,
       localMaxAmount,
       customRoutingOperatorId,
       customRoutingAllowFailover,
       customRoutingProviderId,
       customRoutingDeletedAt,
       customRoutingProviderStatus,
       customRoutingProviderName,
       fixedInternationalAmounts,
       fixedInternationalAmountsWithMarkup,
       fixedLocalAmountsWithMarkup,
       fixedLocalAmounts,
       localAmountCurrencyCode
FROM default_pricing
ORDER BY operatorId