package game;

import java.util.*;

import static game.InOutUtils.readStringsFromInputStream;
import static game.ProcessUtils.UTF_8;

/**
 * Main samplegame class.
 */
public class Main {

    public static void main(String[] args) {
        List<String> input = readStringsFromInputStream(System.in, UTF_8);
        if (!input.isEmpty()) {
            Round round = new Round(input);
            printMovingGroups(makeMove(round));
        }
        System.exit(0);
    }

    private static List<MovingGroup> makeMove(Round round) {
        List<MovingGroup> movingGroups = new ArrayList<>();
        // Место для Вашего кода.
        try {
            List<Planet> myPlanets = round.getOwnPlanets();
            List<Planet> myPlanetstoDefense = new ArrayList();
            boolean flag;
            int ii = 0;
            Planet nearestPlanet = null;
            try {
                List<MovingGroup> enemiesAttack = round.getAdversarysMovingGroups();
                Planet planetISAttacked;
                List<Planet> myPlanet2 = round.getOwnPlanets();
                List<MovingGroup> myGroupstoDefense = round.getOwnMovingGroups();
                boolean flag1 = true;
                for (int j = 0; j < enemiesAttack.size(); j++) {
                    planetISAttacked = round.getPlanets().get(enemiesAttack.get(j).getTo());
                    if ((planetISAttacked.getOwnerTeam() == round.getTeamId()) &&
                            (planetISAttacked.getPopulation() + enemiesAttack.get(j).getStepsLeft() * planetISAttacked.getReproduction() <= enemiesAttack.get(j).getCount())) {
                        Planet planetToAttack1 = planetISAttacked;
                        for (MovingGroup element : myGroupstoDefense)
                            if (element.getTo() == planetISAttacked.getId())
                                flag1 = false;

                        if (flag1) {
                            boolean flag2 = true;

                            myPlanet2.sort((o1, o2) -> {
                                return round.getDistanceMap()[o1.getId()][planetToAttack1.getId()] - round.getDistanceMap()[o2.getId()][planetToAttack1.getId()];
                            });

                            //отправляю помощь с ближайшей планеты, которая может помочь
                            for (Planet element : myPlanet2) {
                                if (flag2) {
                                    if ((element.getPopulation() > enemiesAttack.get(j).getCount())) {
                                        MovingGroup group = new MovingGroup();
                                        group.setFrom(element.getId());
                                        group.setTo(planetISAttacked.getId());
                                        group.setCount(enemiesAttack.get(j).getCount());
                                        movingGroups.add(group);
                                        flag2 = false;
                                        round.getPlanets().get(element.getId()).setPopulation(round.getPlanets().get(element.getId()).getPopulation() - enemiesAttack.get(j).getCount());
                                        myPlanetstoDefense.add(planetISAttacked);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (NullPointerException e) {
            }
            List<Planet> otherPlanets = new ArrayList();
            List<Planet> sentToNoMan = new ArrayList<>();
            boolean f = true;
            for (int i = 0; i < round.getPlanets().size(); i++) {
                if (round.getPlanets().get(i).getOwnerTeam() != round.getTeamId()) {
                    try {
                        List<MovingGroup> myGroups = round.getOwnMovingGroups();
                        f = true;
                        int k = 0;

                        Planet planetToAttack = round.getPlanets().get(i);
                        myPlanets.sort((o1, o2) -> {
                            return round.getDistanceMap()[o1.getId()][planetToAttack.getId()] - round.getDistanceMap()[o2.getId()][planetToAttack.getId()];
                        });

                        int j = 0, countSent = 1;

                        while ((j < myPlanets.size())) {
                            if (!myPlanetstoDefense.contains(myPlanets.get(j))) {
                                if ((round.getPlanets().get(i).getOwnerTeam() == -1) && (myPlanets.get(j).getPopulation() > round.getPlanets().get(i).getPopulation() + 1) && (!sentToNoMan.contains(round.getPlanets().get(i)))) {
                                    MovingGroup group = new MovingGroup();
                                    group.setFrom(myPlanets.get(j).getId());
                                    group.setTo(round.getPlanets().get(i).getId());
                                    group.setCount(round.getPlanets().get(i).getPopulation() + 1);
                                    myPlanets.get(j).setPopulation(myPlanets.get(j).getPopulation() - round.getPlanets().get(i).getPopulation() - 1);
                                    sentToNoMan.add(round.getPlanets().get(i));
                                    movingGroups.add(group);
                                } else if ((myPlanets.get(j).getPopulation() >
                                        (round.getPlanets().get(i).getPopulation() / countSent + round.getDistanceMap()[round.getPlanets().get(i).getId()][myPlanets.get(j).getId() / countSent]) + countSent)) {

                                    MovingGroup group = new MovingGroup();
                                    group.setFrom(myPlanets.get(j).getId());
                                    group.setTo(round.getPlanets().get(i).getId());

                                    group.setCount(round.getPlanets().get(i).getPopulation() / countSent + round.getDistanceMap()[round.getPlanets().get(i).getId()][myPlanets.get(j).getId() / countSent] + countSent);

                                    movingGroups.add(group);

                                    myPlanets.get(j).setPopulation(myPlanets.get(j).getPopulation() - (round.getPlanets().get(i).getPopulation() / countSent + /*round.getPlanets().get(i).getReproduction() **/ round.getDistanceMap()[round.getPlanets().get(i).getId()][myPlanets.get(j).getId() / countSent] + countSent));
                                    countSent++;
                                }
                            }
                            j++;

                        }
                        // }
                    } catch (NullPointerException e) {
                        int j = 0, countSent = 1;
                        while ((f) && (j < myPlanets.size())) {
                            if (!myPlanetstoDefense.contains(myPlanets.get(j))) {
                                if ((round.getPlanets().get(i).getOwnerTeam() == -1) && (myPlanets.get(j).getPopulation() > round.getPlanets().get(i).getPopulation() + 1) && (!sentToNoMan.contains(round.getPlanets().get(i)))) {
                                    MovingGroup group = new MovingGroup();
                                    group.setFrom(myPlanets.get(j).getId());
                                    group.setTo(round.getPlanets().get(i).getId());
                                    group.setCount(round.getPlanets().get(i).getPopulation() + 1);

                                    sentToNoMan.add(round.getPlanets().get(i));

                                    movingGroups.add(group);
                                    myPlanets.get(j).setPopulation(myPlanets.get(j).getPopulation() - round.getPlanets().get(i).getPopulation() - 1);
                                } else if ((myPlanets.get(j).getPopulation() >
                                        (round.getPlanets().get(i).getPopulation() / countSent + round.getDistanceMap()[round.getPlanets().get(i).getId()][myPlanets.get(j).getId() / countSent]) + countSent)) {

                                    MovingGroup group = new MovingGroup();
                                    group.setFrom(myPlanets.get(j).getId());
                                    group.setTo(round.getPlanets().get(i).getId());

                                    group.setCount(round.getPlanets().get(i).getPopulation() / countSent + round.getDistanceMap()[round.getPlanets().get(i).getId()][myPlanets.get(j).getId() / countSent] + countSent);

                                    movingGroups.add(group);

                                    myPlanets.get(j).setPopulation(myPlanets.get(j).getPopulation() - (round.getPlanets().get(i).getPopulation() / countSent + /*round.getPlanets().get(i).getReproduction() **/ round.getDistanceMap()[round.getPlanets().get(i).getId()][myPlanets.get(j).getId() / countSent] + countSent));
                                    countSent++;
                                }
                            }
                            j++;
                        }
                    }
                }
            }
        } catch (
                NullPointerException e) {
        }
        return movingGroups;
    }

    private static Planet getHomePlanet(Round round) {
        Planet home_first;
        if (round.getTeamId() == 0)
            home_first = round.getPlanets().get(0);
        else
            home_first = round.getPlanets().get(round.getPlanetCount() - 1);
        return home_first;
    }

    private static List<Planet> getNoMnPlanets(Round round) {
        List<Planet> noMans = round.getNoMansPlanets();
        List<Planet> res_planet = new ArrayList<>();

        boolean f;

        try {
            List<MovingGroup> myGroups = round.getOwnMovingGroups();
            List<MovingGroup> enemyGroups = round.getAdversarysMovingGroups();

            for (int j = 0; j < noMans.size(); j++) {
                f = true;
                for (int i = 0; i < myGroups.size(); i++) {
                    if (myGroups.get(i).getTo() == noMans.get(j).getId())
                        f = false;
                }
                if (f)
                    res_planet.add(noMans.get(j));
            }
            return res_planet;
        } catch (NullPointerException e) {
            return noMans;
        }
    }

    private static void printMovingGroups(List<MovingGroup> moves) {
        System.out.println(moves.size());
        moves.forEach(move -> System.out.println(move.getFrom() + " " + move.getTo() + " " + move.getCount()));
    }
}