package com.ssafy.user.budget.dto.request;

import java.time.LocalDate;

public record CreateNewBudgetRequest(
    int memberId,
    int monthlyDueDate,
    String name,
    long finalFee,
    LocalDate finalDueDate
) {

}
