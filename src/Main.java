import java.util.Scanner;

// Takım bilgilerini tutan sınıf
class Team {
    String teamName;
    int matchesPlayed;
    int winCount;
    int drawCount;
    int lossCount;
    int goalsFor;       // Atılan gol sayısı
    int goalsAgainst;   // Yenilen gol sayısı
    int totalPoints;

    public Team(String teamName) {
        this.teamName = teamName;
        this.matchesPlayed = 0;
        this.winCount = 0;
        this.drawCount = 0;
        this.lossCount = 0;
        this.goalsFor = 0;
        this.goalsAgainst = 0;
        this.totalPoints = 0;
    }

    // Averaj hesabı (Atılan Gol - Yenilen Gol)
    public int calculateGoalDifference() {
        return this.goalsFor - this.goalsAgainst;
    }
}

// Maç bilgilerini tutan sınıf
class Match {
    Team homeTeam;
    Team awayTeam;
    int homeTeamGoals;
    int awayTeamGoals;

    public Match(Team homeTeam, Team awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeTeamGoals = 0;
        this.awayTeamGoals = 0;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 4 takım tanımlanıyor
        Team teamA = new Team("Team A");
        Team teamB = new Team("Team B");
        Team teamC = new Team("Team C");
        Team teamD = new Team("Team D");

        // Takımları bir diziye koyuyoruz
        Team[] teams = { teamA, teamB, teamC, teamD };

        // 6 maçlık lig fikstürü
        Match[] matches = {
            new Match(teamA, teamB),
            new Match(teamC, teamD),
            new Match(teamA, teamC),
            new Match(teamB, teamD),
            new Match(teamA, teamD),
            new Match(teamB, teamC)
        };

        // 1. Fixture: Fikstürü ekrana yazdırma
        System.out.println("--- TOURNAMENT FIXTURE ---");
        for (int i = 0; i < matches.length; i++) {
            System.out.println("Match " + (i + 1) + ": " + matches[i].homeTeam.teamName + " vs " + matches[i].awayTeam.teamName);
        }
        System.out.println();

        // 1. Score Input & Calculations: Skorları alma ve istatistikleri güncelleme
        System.out.println("--- ENTER MATCH SCORES ---");
        for (int i = 0; i < matches.length; i++) {
            Match currentMatch = matches[i];

            System.out.println("Match " + (i + 1) + ": " + currentMatch.homeTeam.teamName + " vs " + currentMatch.awayTeam.teamName);
            
            System.out.print(currentMatch.homeTeam.teamName + " goals: ");
            currentMatch.homeTeamGoals = scanner.nextInt();

            System.out.print(currentMatch.awayTeam.teamName + " goals: ");
            currentMatch.awayTeamGoals = scanner.nextInt();

            // Oynanan maç sayılarını 1 artırıyoruz
            currentMatch.homeTeam.matchesPlayed = currentMatch.homeTeam.matchesPlayed + 1;
            currentMatch.awayTeam.matchesPlayed = currentMatch.awayTeam.matchesPlayed + 1;

            // Golleri takımların toplamına ekliyoruz
            currentMatch.homeTeam.goalsFor = currentMatch.homeTeam.goalsFor + currentMatch.homeTeamGoals;
            currentMatch.homeTeam.goalsAgainst = currentMatch.homeTeam.goalsAgainst + currentMatch.awayTeamGoals;

            currentMatch.awayTeam.goalsFor = currentMatch.awayTeam.goalsFor + currentMatch.awayTeamGoals;
            currentMatch.awayTeam.goalsAgainst = currentMatch.awayTeam.goalsAgainst + currentMatch.homeTeamGoals;

            // 2. Points Calculation: Sonuca göre puan ve galibiyet/beraberlik/mağlubiyet dağılımı
            if (currentMatch.homeTeamGoals > currentMatch.awayTeamGoals) {
                // Ev sahibi kazandı
                currentMatch.homeTeam.winCount = currentMatch.homeTeam.winCount + 1;
                currentMatch.homeTeam.totalPoints = currentMatch.homeTeam.totalPoints + 3;

                currentMatch.awayTeam.lossCount = currentMatch.awayTeam.lossCount + 1;
            } 
            else if (currentMatch.awayTeamGoals > currentMatch.homeTeamGoals) {
                // Deplasman kazandı
                currentMatch.awayTeam.winCount = currentMatch.awayTeam.winCount + 1;
                currentMatch.awayTeam.totalPoints = currentMatch.awayTeam.totalPoints + 3;

                currentMatch.homeTeam.lossCount = currentMatch.homeTeam.lossCount + 1;
            } 
            else {
                // Beraberlik durumu
                currentMatch.homeTeam.drawCount = currentMatch.homeTeam.drawCount + 1;
                currentMatch.homeTeam.totalPoints = currentMatch.homeTeam.totalPoints + 1;

                currentMatch.awayTeam.drawCount = currentMatch.awayTeam.drawCount + 1;
                currentMatch.awayTeam.totalPoints = currentMatch.awayTeam.totalPoints + 1;
            }

            System.out.println();
        }

        // Maç sonuçlarını toplu olarak ekrana yazdırma
        System.out.println("--- MATCH RESULTS ---");
        for (int i = 0; i < matches.length; i++) {
            System.out.println("Match " + (i + 1) + ": " + matches[i].homeTeam.teamName + " " 
                    + matches[i].homeTeamGoals + " - " + matches[i].awayTeamGoals + " " 
                    + matches[i].awayTeam.teamName);
        }
        System.out.println();

        // 4. Standings Table: Sıralama (Basit Bubble Sort mantığı ile puan ve averaja göre sıralama)
        for (int i = 0; i < teams.length - 1; i++) {
            for (int j = 0; j < teams.length - 1 - i; j++) {
                boolean swapNeeded = false;

                // 1. Kural: Puanı az olan takım aşağıya inmeli
                if (teams[j].totalPoints < teams[j + 1].totalPoints) {
                    swapNeeded = true;
                }
                // 2. Kural: Puanlar eşitse, averajı az olan takım aşağıya inmeli
                else if (teams[j].totalPoints == teams[j + 1].totalPoints) {
                    if (teams[j].calculateGoalDifference() < teams[j + 1].calculateGoalDifference()) {
                        swapNeeded = true;
                    }
                }

                // Takımların yerini değiştirme (Swap)
                if (swapNeeded) {
                    Team temporaryTeam = teams[j];
                    teams[j] = teams[j + 1];
                    teams[j + 1] = temporaryTeam;
                }
            }
        }

        // Puan Durumu Tablosunu Yazdırma (Sekme karakteri '\t' ile düzgün aralık)
        System.out.println("--- STANDINGS TABLE ---");
        System.out.println("Team\t\tPlayed\tWon\tDrawn\tLost\tGF\tGA\tGD\tPoints");
        System.out.println("-------------------------------------------------------------------------");

        for (int i = 0; i < teams.length; i++) {
            Team currentTeam = teams[i];
            System.out.println(currentTeam.teamName + "\t\t" 
                    + currentTeam.matchesPlayed + "\t" 
                    + currentTeam.winCount + "\t" 
                    + currentTeam.drawCount + "\t" 
                    + currentTeam.lossCount + "\t" 
                    + currentTeam.goalsFor + "\t" 
                    + currentTeam.goalsAgainst + "\t" 
                    + currentTeam.calculateGoalDifference() + "\t" 
                    + currentTeam.totalPoints);
        }
        System.out.println();

        // 5. Champion Announcement: Sıralamada 1. sırada yer alan takım şampiyondur
        Team championTeam = teams[0];
        System.out.println("--- TOURNAMENT WINNER ---");
        System.out.println("Tournament Champion: " + championTeam.teamName);

        scanner.close();
    }
}