package com.xorg.wo;

import com.xorg.wo.optimizer.WorkforceOptimizer;
import com.xorg.wo.proc.DataLoader;
import com.xorg.wo.proc.EmployeeHierarchyBuilder;

public class Application {

    public static void main(String[] args) {
        DataLoader dataLoader = new DataLoader();
        EmployeeHierarchyBuilder employeeHierarchyBuilder = new EmployeeHierarchyBuilder(dataLoader);
        WorkforceOptimizer optimizer = new WorkforceOptimizer(employeeHierarchyBuilder);
        optimizer.optimizeWorkforce();
    }
}
