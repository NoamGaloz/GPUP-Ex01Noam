package gpup.jaxb.schema.parser;

import gpup.component.target.Target;
import gpup.component.target.TargetType;
import gpup.component.targetgraph.TargetGraph;
import gpup.exception.TargetExistException;
import gpup.jaxb.schema.generated.GPUPDescriptor;
import gpup.jaxb.schema.generated.GPUPTarget;
import gpup.jaxb.schema.generated.GPUPTargetDependencies;
import gpup.jaxb.schema.generated.GPUPTargets;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public abstract class GPUPParser {
    public static TargetGraph parseTargetGraph(GPUPDescriptor gpupDescriptor) throws TargetExistException, NoSuchElementException {
        TargetGraph targetGraph = null;
        Map<String, Target> targetMap = null;
        String name = gpupDescriptor.getGPUPConfiguration().getGPUPGraphName();
        String workingDirectory = gpupDescriptor.getGPUPConfiguration().getGPUPWorkingDirectory();

        targetGraph = new TargetGraph(name);
        targetGraph.setWorkingDirectory(workingDirectory);
        targetMap = parseTargetList(gpupDescriptor.getGPUPTargets());
        targetGraph.buildGraph(targetMap);

        return targetGraph;
    }

    private static Target createTarget(GPUPTarget gpupTarget) {
        String name = gpupTarget.getName();
        Target target = new Target(name);

        target.setUserData(gpupTarget.getGPUPUserData());

        return target;
    }

    private static Map<String, Target> parseTargetList(GPUPTargets gpupTargets) {
        Map<String, Target> targets = new HashMap<>();

        // creating a map of targets (no dependencies yet):
        for (GPUPTarget gt : gpupTargets.getGPUPTarget()) {
            if (!targets.containsKey(gt.getName())) {
                Target target = createTarget(gt);
                targets.put(target.getName(), target);
            } else {
                throw new TargetExistException(gt.getName());
            }
        }
        // update the dependencies of each target:
        updateDependencies(targets, gpupTargets);
        updateTargetsType(targets);
        return targets;
    }

    private static void updateDependencies(Map<String, Target> targets, GPUPTargets gpupTargets) throws NoSuchElementException {
        final String DEPENDS_ON = "dependsOn";
        final String REQUIRED_FOR = "requiredFor";

        for (GPUPTarget gt : gpupTargets.getGPUPTarget()) {
            Target target = targets.get(gt.getName());
            if (gt.getGPUPTargetDependencies() != null) {
                for (GPUPTargetDependencies.GPUGDependency dependency : gt.getGPUPTargetDependencies().getGPUGDependency()) {

                    if (validDependency(dependency, gt.getName()) && targets.containsKey(dependency.getValue())) {
                        if (dependency.getType().equals(DEPENDS_ON)) {
                            target.addDependOnTarget(targets.get(dependency.getValue()));
                            targets.get(dependency.getValue()).addRequiredForTarget(target);
                        } else if (dependency.getType().equals(REQUIRED_FOR)) {
                            target.addRequiredForTarget(targets.get(dependency.getValue()));
                            targets.get(dependency.getValue()).addDependOnTarget(target);
                        }

                        // check for 2-way circle:
                        if (!isTwoWayCircle(target, targets.get(dependency.getValue()), dependency.getType())) {
                            throw new NoSuchElementException("There is a conflict between targets: " + target.getName() + ", " + dependency.getValue());
                        }
                    } else {
                        throw new NoSuchElementException("There was a try to add dependency of a non-existing target");
                    }
                }
            }
        }
    }

    private static void updateTargetsType(Map<String, Target> targets) {

        for (Target target : targets.values()) {
            if (target.getDependsOnList().size() == 0 && target.getRequiredForList().size() == 0) {
                target.setType(TargetType.Independent);
            } else if (target.getDependsOnList().size() != 0 && target.getRequiredForList().size() != 0) {
                target.setType(TargetType.Middle);
            } else if (target.getRequiredForList().size() == 0) {
                target.setType(TargetType.Root);
            } else {
                target.setType(TargetType.Leaf);
            }
        }
    }

    private static boolean isTwoWayCircle(Target target1, Target target2, String type) {
        if (target1.isDependency(target2, type) && target2.isDependency(target1, type)) {
            return false;
        }
        return true;
    }


    private static boolean validDependency(GPUPTargetDependencies.GPUGDependency dependency, String name) {
        if (dependency.getType() == null || dependency.getValue() == null || dependency.getValue() == name) {
            throw new NoSuchElementException("One of the dependencies of " + name + " is wrong.");
        }
        return true;
    }


}
