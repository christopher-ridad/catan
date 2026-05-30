# BVA for `ResourceProduction`

## Method under test: `distributeResources(int roll, List<Vertex> vertices, Bank bank)`

- **TC1: DistributeResources_RollOf7_NoResourcesDistributed** ( :x: )
    - **State of the system**: roll = 7, one player has a settlement, bank is full
    - **Expected output**: no player receives any resources (7 triggers robber, not production)

- **TC2: DistributeResources_RollOf2_SettlementAdjacent_PlayerReceives1** ( :x: )
    - **State of the system**: roll = 2 (minimum producing roll), one HILLS hex with token 2,
      one player has settlement on adjacent vertex, bank has >= 1 BRICK
    - **Expected output**: player's BRICK count increases by 1

- **TC3: DistributeResources_RollOf12_SettlementAdjacent_PlayerReceives1** ( :x: )
    - **State of the system**: roll = 12 (maximum producing roll), one FIELDS hex with token 12,
      one player has settlement on adjacent vertex, bank has >= 1 GRAIN
    - **Expected output**: player's GRAIN count increases by 1

- **TC4: DistributeResources_CityAdjacent_PlayerReceives2** ( :x: )
    - **State of the system**: roll = 6, one HILLS hex with token 6, one player has city on
      adjacent vertex (upgradeToCity called), bank has >= 2 BRICK
    - **Expected output**: player's BRICK count increases by 2

- **TC5: DistributeResources_BankHas0_NoOneReceives** ( :x: )
    - **State of the system**: roll = 6, one HILLS hex with token 6, one player has settlement
      adjacent, bank has 0 BRICK
    - **Expected output**: player's BRICK count stays 0 (bank empty, distribution skipped)

- **TC6: DistributeResources_UnoccupiedVertexAdjacent_NoResourceDistributed** ( :x: )
    - **State of the system**: roll = 4, one hex with token 4, all adjacent vertices unoccupied
    - **Expected output**: no player receives any resources

- **TC7: DistributeResources_MultiplePlayersAdjacentSameHex_AllReceive** ( :x: )
    - **State of the system**: roll = 8, one HILLS hex with token 8, two different players each
      have a settlement on separate adjacent vertices, bank has >= 2 BRICK
    - **Expected output**: both players' BRICK count increases by 1 each

- **TC8: DistributeResources_BankCannotCoverAll_NobodyReceives** ( :x: )
    - **State of the system**: roll = 8, one HILLS hex with token 8, two players each have a
      settlement adjacent (total needed = 2 BRICK), bank has only 1 BRICK
    - **Expected output**: neither player receives BRICK (bank cannot fully cover demand)
