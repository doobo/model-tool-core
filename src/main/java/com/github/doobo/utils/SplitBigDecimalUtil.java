package com.github.doobo.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class SplitBigDecimalUtil {

    /**
     * 金额平摊，保留最后的一分钱
     */
    public static BigDecimal[] splitWithRemainder(BigDecimal total, int parts) {
        if(parts < 2) {
            BigDecimal[] decimals = new BigDecimal[1];
            decimals[0] = total;
            return decimals;
        }
        BigDecimal[] amounts = new BigDecimal[parts];
        BigDecimal baseAmount = total.divide(new BigDecimal(parts), 2, RoundingMode.DOWN);
        BigDecimal remainder = total.subtract(baseAmount.multiply(new BigDecimal(parts)));
        // 余数转换为"分"的单位
        int remainderCents = remainder.multiply(new BigDecimal(100)).intValue();
        for (int i = 0; i < parts; i++) {
            amounts[i] = baseAmount;
            if (i < remainderCents) {
                amounts[i] = amounts[i].add(new BigDecimal("0.01"));
            }
        }
        return amounts;
    }

    /**
     * 根据子订单金额比例平摊总金额
     * @param totalAmountStr 总金额字符串
     * @param scale 小数位数
     * @param roundingMode 舍入模式
     * @return 平摊后的金额列表
     * @throws IllegalArgumentException 如果参数无效
     */
    public static List<BigDecimal> allocateAmount(String totalAmountStr, List<String> subAmountList
            , int scale, RoundingMode roundingMode) {
        // 参数校验
        if (totalAmountStr == null || subAmountList == null || subAmountList.isEmpty()) {
            throw new IllegalArgumentException("参数不能为空");
        }
        // 转换为BigDecimal
        BigDecimal totalAmount = new BigDecimal(totalAmountStr);
        List<BigDecimal> subAmounts = subAmountList.stream()
                .map(BigDecimal::new)
                .collect(Collectors.toList());
        // 计算子订单总金额
        BigDecimal subTotal = subAmounts.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 如果子订单总金额为0，则平均分配
        if (subTotal.compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal avgAmount = totalAmount.divide(
                    new BigDecimal(subAmounts.size()), scale, roundingMode);
            return subAmounts.stream()
                    .map(amount -> avgAmount)
                    .collect(Collectors.toList());
        }
        // 按比例分配
        List<BigDecimal> allocatedAmounts = subAmounts.stream()
                .map(amount -> {
                    BigDecimal ratio = amount.divide(subTotal, scale + 2, RoundingMode.HALF_UP);
                    return totalAmount.multiply(ratio).setScale(scale, roundingMode);
                })
                .collect(Collectors.toList());
        // 处理精度问题，确保总和等于总金额
        BigDecimal sumAllocated = allocatedAmounts.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (sumAllocated.compareTo(totalAmount) != 0) {
            BigDecimal difference = totalAmount.subtract(sumAllocated);
            int i = 0;
            BigDecimal newValue = null;
            for(BigDecimal subAmount : allocatedAmounts) {
                if(BigDecimal.ZERO.compareTo(subAmount) == 0) {
                    i++;
                    continue;
                }
                newValue = subAmount.add(difference);
                break;
            }
            if(Objects.nonNull(newValue)) {
                allocatedAmounts.set(i, newValue);
            }
        }
        return allocatedAmounts;
    }
}
