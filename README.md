# PetriNet Modelling of a Rail Network
The network of interest is given as follows, which consists of 11 sections.

![Figure 1: Whole Network](PetriDiagram/WholeNetwork.png)

The network can be further divided into the passenger line: 
![Figure 2: Passenger Line](PetriDiagram/PassengerLine.png)
and the freight line connecting to the Islington Workshop:
![Figure 3: Passenger Line](PetriDiagram/FreightLine.png)
The two lines are disconnected and trains cannot be move from passenger line to freight line and vice versa. 

# Train movements
Trains can travel N-S (left - right)
- Entry: 1, 3.
- Exit: 4, 8, 9, 11.

Train can travel S-N (right - left)
- Entry: 4, 9, 10, 11
- Exit: 2, 3

Based on the project brief, the following paths are legal: 

North-South: 
- 1, 5, 8
- 1, 5, 9
- 3, 4
- 3, 7, 11

South-North: 
- 4, 3
- 9, 6, 2
- 10, 6, 2
- 11, 7, 3

# Network Segment Descriptions: 
Based on the set of train movements, we can classify different rail sections based on their connectivity characterisitcs. 

## Straight section
### One way section
One-way section allows trains to move in only one direction - i.e. either North to South or South to North. A place-transition structure of section 2 is shown:  

![Figure 3: One way Network](./PetriDiagram/OneWay.drawio.svg)

Transitions are named based on the following structure: t<section_from><section_to>. For instance, $t_{62}$ indicates a transition from section 6 to section 2. Transitions that involve section 0 indicate entry/exit. For instance $t_{20}$ indicates the exit transition from section 2. A marking of a place $p_i$ is indicated as $m_i$ and is shown diagramatically as a dot. Unless otherwise stated, all  places in the PetriNet used have a maximum capacity of 1 - i.e. $m_2 \leq 1$. We will see later how a monitor place can be used to enforce capacity constraints. 

## Two-way section
Two-way section allows trains to move in both directions. A place-transition structure of section 11 is given as follows:

![Figure 4: Two way Network](./PetriDiagram/TwoWays.drawio.svg)

Two-way sections contain two places, corresponding to N-S direction (L) and S-N direction (R). It is noted that some papers uses a simplified version of the above place-transition structure:

![Figure 5: Two way Network Simplified](./PetriDiagram/TwoWaySimplified.drawio.svg)

However, I decided to keep the un-simplified version to make explicit that trains that are moving N-S cannot abruptly turn around and move S-N and vice-versa. 

## Junction 
When two paths intersect, a constraint must be placed to prevent multiple trains in the same intersection which can cause accidents to occur. We shall use the point structure as described in ![ref] to control movements of trains in intersection. Consider the intersection between path <1,5> and path <3,4>:

![Figure 6: Junction](./PetriDiagram/Junction.drawio.svg)

A train passing the junction from section 1 to 5 is represented by the firing of transition $t_{15}$, which requires a marking at $p_{1L}$ (a train is currently present in section 1) and a marking at place $R_1$ of the point system (right path is available for passing). Similarly, passage from section 3 to section 4 requires markings at $p_{3L}$ and $L_1$. Initially, $R_1$ is marked to indicate passenger trains having higher passage priority than freight trains at junction. 

Marking can be changed from $L1$ to $R1$ via the transition $t_{L1j1}$ to place $j_1$ and then firing transition $t_{j1R1}$ to place $R1$, which now enables freight passage. 

## Capacity Constraint 

