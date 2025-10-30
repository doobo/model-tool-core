package com.github.doobo.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public abstract class DoNumberUtils {

    /**
     * 对二维列表的每一列求和（使用 BigDecimal）
     * @param matrix 二维列表
     * @return 每一列的和（字符串形式）
     */
    public static List<String> sumColumns(List<List<String>> matrix) {
        if (matrix == null || matrix.isEmpty()) {
            return new ArrayList<>();
        }
        // 获取列数（假设所有行的列数相同）
        int numColumns = matrix.get(0).size();

        // 初始化结果列表
        List<BigDecimal> columnSums = new ArrayList<>(numColumns);
        for (int i = 0; i < numColumns; i++) {
            columnSums.add(BigDecimal.ZERO); // 初始化为 0
        }

        // 遍历每一行，累加每一列的值
        for (List<String> row : matrix) {
            for (int i = 0; i < numColumns; i++) {
                // 将字符串转换为 BigDecimal
                String col = row.get(i);
                if(DoObjectUtils.isBlank(col)){
                    col = "0";
                }
                BigDecimal value = new BigDecimal(col);
                // 累加列值
                columnSums.set(i, columnSums.get(i).add(value));
            }
        }
        // 将 BigDecimal 结果转换为字符串
        List<String> result = new ArrayList<>();
        for (BigDecimal sum : columnSums) {
            result.add(sum.setScale(2, RoundingMode.HALF_UP)
                    .stripTrailingZeros().toPlainString());
        }
        return result;
    }


    /**
     * 对二维列表中的所有值求和（使用 BigDecimal）
     * @param matrix 二维列表
     * @return 所有值的和
     */
    public static String sumArrayList(List<List<String>> matrix, int scale, RoundingMode roundingMode) {
        // 初始化总和为 0
        BigDecimal totalSum = BigDecimal.ZERO;
        // 遍历每一行
        for (List<String> row : matrix) {
            // 遍历每一列
            for (String value : row) {
                String col = value;
                if(DoObjectUtils.isBlank(col)){
                    col = "0";
                }
                // 将字符串转换为 BigDecimal 并累加
                totalSum = totalSum.add(new BigDecimal(col));
            }
        }
        return totalSum.setScale(scale, roundingMode)
                .stripTrailingZeros().toPlainString();
    }

    /**
     * 对二维列表中的所有值求和（使用 BigDecimal）
     * @param matrix 二维列表
     * @return 所有值的和
     */
    public static String sumAllList(List<List<String>> matrix) {
        return sumArrayList(matrix, 2, RoundingMode.HALF_UP);
    }

    /**
     * 求和一维数组
     */
    public static String sumList(List<String> matrix) {
        return sumArrayList(Collections.singletonList(matrix), 2, RoundingMode.HALF_UP);
    }
}
