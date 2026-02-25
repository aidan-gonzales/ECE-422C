/**
 * ECE 422C - Shogun's Challenge Lab
 * 
 * Author: <Aidan Gonzales>
 * EID: <AGG3498>
 * Date: <1/28/26>
 * 
 * Represents a chamber in the Shogun's castle.
 * Each chamber contains one Oni to defeat and one Artifact to collect.
 */
final class Chamber {
    
    private String name;
    private Oni oni;
    private Artifact artifact;

    /**
     * Constructs a new Chamber.
     * @param name     The chamber's name
     * @param oni      The Oni guarding this chamber
     * @param artifact The Artifact found after defeating the Oni
     */
    public Chamber(String name, Oni oni, Artifact artifact) {
        this.name = name;
        this.oni = oni;
        this.artifact = artifact;
    }

    //getter methods:

    public String getName() {
        return name;
    }

    public Oni getOni() {
        return oni;
    }

    public Artifact getArtifact() {
        return artifact;
    }
}
