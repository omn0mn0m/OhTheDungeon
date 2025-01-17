/* 
 * Copyright (C) 2021 shadow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package otd.dungeon.aetherlegacy;

import forge_sandbox.com.someguyssoftware.gottschcore.enums.Rarity;
import forge_sandbox.com.someguyssoftware.gottschcore.positional.Coords;
import forge_sandbox.com.someguyssoftware.gottschcore.positional.ICoords;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import otd.lib.async.later.aetherlegacy.Chest_Later;
import otd.lib.async.later.aetherlegacy.Spawner_Later;

public class ComponentSilverDungeon extends AetherStructure {

    private static final Material LOCKED_ANGELIC_STONE = BlocksAether.locked_angelic_stone;

    private static final Material LOCKED_LIGHT_ANGELIC_STONE = BlocksAether.locked_light_angelic_stone;

    private int[][][] rooms = new int[3][3][3];

    private int firstStaircaseZ, secondStaircaseZ, finalStaircaseZ;

    private int xTendency, zTendency;

    public ComponentSilverDungeon() {

    }

    public ComponentSilverDungeon(int chunkX, int chunkZ) {
//        this.coordBaseMode = 0;
//        this.boundingBox = new StructureBoundingBox(chunkX, 80, chunkZ, chunkX + 100, 220, chunkZ + 100);
    }

    public void setStaircasePosition(int first, int second, int third) {
        this.firstStaircaseZ = first;
        this.secondStaircaseZ = second;
        this.finalStaircaseZ = third;
    }

    public void setCloudTendencies(int xTendency, int zTendency) {
        this.xTendency = xTendency;
        this.zTendency = zTendency;
    }
    
    private final static class BlocksAether {
        public static Material holystone = Material.COBBLESTONE;
        public static Material mossy_holystone = Material.MOSSY_COBBLESTONE;
        public static Material angelic_trap = Material.CHISELED_STONE_BRICKS;//yellow
        public static Material chest_mimic = Material.TRAPPED_CHEST;
        public static Material ambrosium_torch = Material.TORCH;
        public static Material treasure_chest = Material.LIME_SHULKER_BOX;
        public static Material aether_dirt = Material.DIRT;
        public static Material golden_oak_sapling = Material.OAK_SAPLING;
        public static Material pillar = Material.QUARTZ_PILLAR;
        public static Material pillar_top = Material.CHISELED_QUARTZ_BLOCK;
        public static Material locked_angelic_stone = Material.STONE_BRICKS;//blue
        public static Material locked_light_angelic_stone = Material.MOSSY_STONE_BRICKS;//green
        public static Material aercloud = Material.PACKED_ICE;
    }
    
    public static void generateClouds(AetherStructure structure, Material block, boolean isFlat, int cloudSize, int offsetX, int offsetY, int offsetZ, int xTendency, int zTendency) {
        int x = offsetX;
        int y = offsetY;
        int z = offsetZ;

        if (block == null) {
            return;
        }

        for (int n = 0; n < cloudSize; ++n) {
            x += structure.random.nextInt(3) - 1 + xTendency;

            if (structure.random.nextBoolean() && !isFlat || isFlat && structure.random.nextInt(10) == 0) {
                y += structure.random.nextInt(3) - 1;
            }

            z += structure.random.nextInt(3) - 1 + zTendency;

            for (int x1 = x; x1 < x + structure.random.nextInt(4) + 3 * (isFlat ? 3 : 1); ++x1) {
                for (int y1 = y; y1 < y + structure.random.nextInt(1) + 2; ++y1) {
                    for (int z1 = z; z1 < z + structure.random.nextInt(4) + 3 * (isFlat ? 3 : 1); ++z1) {
                        if (structure.getBlockState(x1, y1, z1) == Material.AIR) {
                            if (Math.abs(x1 - x) + Math.abs(y1 - y) + Math.abs(z1 - z) < 4 * (isFlat ? 3 : 1) + structure.random.nextInt(2)) {
                                structure.setBlockWithOffset(x1, y1, z1, block);
                            }
                        }
                    }
                }
            }
        }
    }


    @Override
    public boolean generate(boolean cloud, Material cloud_material) {
        this.replaceAir = true;

        this.setStructureOffset(21, 17, 20);
        if(cloud) {
            for (int tries = 0; tries < 100; tries++) {
                generateClouds(this, cloud_material, false, 10, this.random.nextInt(77), 0, this.random.nextInt(50), this.xTendency, this.zTendency);
            }
        }

        this.setStructureOffset(31, 24, 30);

        this.replaceSolid = true;

        this.setBlocks(BlocksAether.holystone, BlocksAether.mossy_holystone, 30);

        this.addSolidBox(0, -5, 0, 55, 5, 30);

        for (int x = 0; x < 55; x += 4) {
            this.generateColumn(x, 0, 0, 14);
            this.generateColumn(x, 0, 27, 14);
        }

        for (int z = 0; z < 12; z += 4) {
            this.generateColumn(0, 0, z, 14);
            this.generateColumn(52, 0, z, 14);
        }

        for (int z = 19; z < 30; z += 4) {
            this.generateColumn(0, 0, z, 14);
            this.generateColumn(52, 0, z, 14);
        }

        this.setBlocks(LOCKED_ANGELIC_STONE, LOCKED_LIGHT_ANGELIC_STONE, 20);
        this.addHollowBox(4, -1, 4, 47, 16, 22);
        this.addPlaneX(11, 0, 5, 15, 20);
        this.addPlaneX(18, 0, 5, 15, 20);
        this.addPlaneX(25, 0, 5, 15, 20);
        this.addPlaneZ(5, 0, 11, 20, 15);
        this.addPlaneZ(5, 0, 18, 20, 15);

        this.setBlocks(LOCKED_ANGELIC_STONE, BlocksAether.angelic_trap, 30);
        this.addPlaneY(5, 4, 5, 20, 20);
        this.addPlaneY(5, 9, 5, 20, 20);

        for (int y = 0; y < 2; y++) {
            for (int z = 14; z < 16; z++) {
                this.setBlockWithOffset(4, y, z, Material.AIR);
            }
        }

        this.setBlocks(Material.AIR, Material.AIR, 1);
        this.addSolidBox(0, -4, 14, 1, 4, 2);
        this.addSolidBox(1, -3, 14, 1, 3, 2);
        this.addSolidBox(2, -2, 14, 1, 2, 2);
        this.addSolidBox(3, -1, 14, 1, 1, 2);

        this.setBlocks(LOCKED_ANGELIC_STONE, LOCKED_LIGHT_ANGELIC_STONE, 15);

        for (int y = 0; y < 7; y++) {
            this.addPlaneY(-1, 15 + y, -1 + 2 * y, 57, 32 - 4 * y);
        }

        this.generateStaircase(19, 0, 5 + this.finalStaircaseZ * 7, 10);

        this.rooms[2][0][this.finalStaircaseZ] = 2;
        this.rooms[2][1][this.finalStaircaseZ] = 2;
        this.rooms[2][2][this.finalStaircaseZ] = 1;

        int x = 25;
        int y;
        int z;

        for (y = 0; y < 2; y++) {
            for (z = 7 + 7 * this.finalStaircaseZ; z < 9 + 7 * this.finalStaircaseZ; z++) {
                this.setBlockWithOffset(x, y, z, Material.AIR);
            }
        }

        this.generateStaircase(12, 0, 5 + this.firstStaircaseZ * 7, 5);

        this.rooms[1][0][this.firstStaircaseZ] = 1;
        this.rooms[1][1][this.firstStaircaseZ] = 1;

        this.generateStaircase(5, 5, 5 + this.secondStaircaseZ * 7, 5);

        this.rooms[0][1][this.secondStaircaseZ] = 1;
        this.rooms[0][2][this.secondStaircaseZ] = 1;

        for (int p = 0; p < 3; p++) {
            for (int q = 0; q < 3; q++) {
                for (int r = 0; r < 3; r++) {
                    if (p == 0 && q != 0 && this.secondStaircaseZ == r) {
                        if (r == 0) {
                            this.generateDoorX(11 + 7 * p, 5 * q, 7 + 7 * r, 2, 2);
                            this.generateDoorZ(-3 + 7 * r, 7 + 7 * p, 5 * q, 2, 2);
                        } else if (r == 1) {
                            this.generateDoorX(11 + 7 * p, 5 * q, 7 + 7 * r, 2, 2);
                            this.generateDoorZ(4 + 7 * r, 7 + 7 * p, 5 * q, 2, 2);
                            this.generateDoorZ(11 + 7 * r, 7 + 7 * p, 5 * q, 2, 2);
                        } else if (r == 2) {
                            this.generateDoorX(11 + 7 * p, 5 * q, 7 + 7 * r, 2, 2);
                            this.generateDoorZ(4 + 7 * r, 7 + 7 * p, 5 * q, 2, 2);
                        }
                    } else if (p == 1 && q != 2 && this.firstStaircaseZ == r) {
                        if (this.firstStaircaseZ != this.finalStaircaseZ) {
                            this.generateDoorX(11 + 7 * p, 5 * q, 7 + 7 * r, 2, 2);
                        }

                        if (r == 0) {
                            this.generateDoorZ(11 + 7 * r, 7 + 7 * p, 5 * q, 2, 2);
                            this.generateDoorX(4 + 7 * p, 5 * q, 7 + 7 * r, 2, 2);
                        } else if (r == 1) {
                            this.generateDoorZ(4 + 7 * r, 7 + 7 * p, 5 * q, 2, 2);
                            this.generateDoorZ(11 + 7 * r, 7 + 7 * p, 5 * q, 2, 2);
                            this.generateDoorX(4 + 7 * p, 5 * q, 7 + 7 * r, 2, 2);
                        } else if (r == 2) {
                            this.generateDoorZ(4 + 7 * r, 7 + 7 * p, 5 * q, 2, 2);
                            this.generateDoorX(4 + 7 * p, 5 * q, 7 + 7 * r, 2, 2);
                        }
                    } else if (p == 2 && this.finalStaircaseZ == r) {
                        if (q == 0) {
                            this.generateDoorX(11 + 7 * p, 5 * q, 7 + 7 * r, 2, 2);
                        } else if (q == 2) {
                            if (r == 0) {
                                this.generateDoorX(4 + 7 * p, 5 * q, 7 + 7 * r, 2, 2);
                                this.generateDoorZ(11 + 7 * r, 7 + 7 * p, 5 * q, 2, 2);
                            } else if (r == 1) {
                                this.generateDoorX(4 + 7 * p, 5 * q, 7 + 7 * r, 2, 2);
                                this.generateDoorZ(4 + 7 * r, 7 + 7 * p, 5 * q, 2, 2);
                                this.generateDoorZ(11 + 7 * r, 7 + 7 * p, 5 * q, 2, 2);
                            } else if (r == 2) {
                                this.generateDoorX(4 + 7 * p, 5 * q, 7 + 7 * r, 2, 2);
                                this.generateDoorZ(4 + 7 * r, 7 + 7 * p, 5 * q, 2, 2);
                            }
                        }
                    } else {
                        int type = this.rooms[p][q][r];

                        if (p + 1 < 3) {
                            int newType = this.rooms[p + 1][q][r];

                            if (newType != 2 && !(newType == 1 && type == 1)) {
                                this.rooms[p][q][r] = 3;
                                type = 3;

                                this.generateDoorX(11 + 7 * p, 5 * q, 7 + 7 * r, 2, 2);
                            }
                        }

                        if (p - 1 > 0) {
                            int newType = this.rooms[p - 1][q][r];

                            if (newType != 2 && !(newType == 1 && type == 1)) {
                                this.rooms[p][q][r] = 4;
                                type = 4;

                                this.generateDoorX(4 + 7 * p, 5 * q, 7 + 7 * r, 2, 2);
                            }
                        }

                        if (r + 1 < 3) {
                            int newType = this.rooms[p][q][r + 1];

                            if (newType != 2 && !(newType == 1 && type == 1)) {
                                this.rooms[p][q][r] = 5;
                                type = 5;

                                this.generateDoorZ(11 + 7 * r, 7 + 7 * p, 5 * q, 2, 2);
                            }
                        }

                        if (r - 1 > 0) {
                            int newType = this.rooms[p][q][r - 1];

                            if (newType != 2 && !(newType == 1 && type == 1)) {
                                this.rooms[p][q][r] = 6;
                                type = 6;

                                this.generateDoorZ(4 + 7 * r, 7 + 7 * p, 5 * q, 2, 2);
                            }
                        }

                        int roomType = this.random.nextInt(3);

                        if (type >= 3) {
                            this.setBlockWithOffset(7 + p * 7, -1 + q * 5, 7 + r * 7, BlocksAether.angelic_trap);

                            switch (roomType) {
                                case 1: {
                                    this.addPlaneY(7 + 7 * p, 5 * q, 7 + 7 * r, 2, 2);

                                    int u = 7 + 7 * p + this.random.nextInt(2);
                                    int v = 7 + 7 * r + this.random.nextInt(2);

                                    if (this.getBlockState(u, 5 * q + 1, v) == Material.AIR) {
                                        ICoords chestCoords = new Coords(u + this.startX, 5 * q + 1 + this.startY, v + this.startZ);
                                        Chest_Later.generate_later(this.worldObj, random, chestCoords, Rarity.RARE, Material.CHEST);
                                    }

                                    break;
                                }
                                case 2: {
                                    this.addPlaneY(7 + 7 * p, 5 * q, 7 + 7 * r, 2, 2);
                                    ICoords spawnerCoords = new Coords(7 + 7 * p + this.random.nextInt(2) + this.startX,
                                            5 * q + 1 + this.startY, 7 + 7 * r + this.random.nextInt(2) + this.startZ);
                                    Spawner_Later.generate_later(worldObj, random, spawnerCoords, EntityType.VEX);

                                    if (this.random.nextBoolean()) {
                                        //this.setBlockWithOffset(7 + 7 * p + this.random.nextInt(2), 5 * q + 1, 7 + 7 * r + this.random.nextInt(2), BlocksAether.chest_mimic);
                                        ICoords chestCoords = new Coords(7 + 7 * p + this.random.nextInt(2) + this.startX,
                                                5 * q + 1 + this.random.nextInt(2) + this.startY, 
                                                7 + 7 * r + this.random.nextInt(2) + this.startZ);
                                        Chest_Later.generate_later(this.worldObj, random, chestCoords, Rarity.RARE, Material.CHEST);
                                    }

                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        for (x = 0; x < 24; x++) {
            for (z = 0; z < 20; z++) {
                int distance = (int) (Math.sqrt(x * x + (z - 7) * (z - 7)) + Math.sqrt(x * x + (z - 12) * (z - 12)));

                if (distance == 21) {
                    this.setBlockWithOffset(26 + x, 0, 5 + z, LOCKED_LIGHT_ANGELIC_STONE);
                } else if (distance > 21) {
                    this.setBlockWithOffset(26 + x, 0, 5 + z, LOCKED_ANGELIC_STONE);
                }
            }
        }

        this.setBlocks(LOCKED_ANGELIC_STONE, LOCKED_LIGHT_ANGELIC_STONE, 20);
        this.addPlaneY(44, 1, 11, 6, 8);
        this.addSolidBox(46, 2, 13, 4, 2, 4);
        this.addLineX(46, 4, 13, 4);
        this.addLineX(46, 4, 16, 4);
        this.addPlaneX(49, 4, 13, 4, 4);

        this.setBlocks(Material.BLUE_WOOL, Material.BLUE_WOOL, 20);
        this.addPlaneY(47, 3, 14, 2, 2);

        for (x = 0; x < 2; x++) {
            for (z = 0; z < 2; z++) {
                this.setBlockWithOffset(44 + x * 5, 2, 11 + z * 7, BlocksAether.ambrosium_torch);
            }
        }

        this.setBlocks(LOCKED_ANGELIC_STONE, LOCKED_LIGHT_ANGELIC_STONE, 20);
        this.addPlaneY(35, 1, 5, 6, 3); //left
        this.addPlaneY(35, 1, 22, 6, 3); //right

        //left
        this.addLineZ(34, 1, 5, 2);
        this.addLineZ(41, 1, 5, 2);
        this.addLineX(36, 1, 8, 4);
        //right
        this.addLineZ(34, 1, 23, 2);
        this.addLineZ(41, 1, 23, 2);
        this.addLineX(36, 1, 21, 4);

        this.setBlocks(Material.WATER, Material.WATER, 1);
        this.addPlaneY(35, 1, 5, 6, 3);
        this.addPlaneY(35, 1, 22, 6, 3);

        this.setBlockWithOffset(35, 1, 7, LOCKED_ANGELIC_STONE);
        this.setBlockWithOffset(40, 1, 7, LOCKED_ANGELIC_STONE);
        this.setBlockWithOffset(35, 1, 22, LOCKED_ANGELIC_STONE);
        this.setBlockWithOffset(40, 1, 22, LOCKED_ANGELIC_STONE);

        for (x = 36; x < 40; x += 3) {
            for (z = 8; z < 22; z += 13) {
                this.setBlockWithOffset(x, 2, z, BlocksAether.ambrosium_torch);
            }
        }

        this.generateChandelier(28, 0, 10, 8);
        this.generateChandelier(43, 0, 10, 8);
        this.generateChandelier(43, 0, 19, 8);
        this.generateChandelier(28, 0, 19, 8);

        this.generateGoldenOakSapling(45, 1, 6);
        this.generateGoldenOakSapling(45, 1, 21);

        this.setBlocks(LOCKED_ANGELIC_STONE, LOCKED_LIGHT_ANGELIC_STONE, 20);
        this.addHollowBox(41, -2, 13, 4, 4, 4);

        x = 42 + this.random.nextInt(2);
        z = 14 + this.random.nextInt(2);
        
        this.setBlockWithOffset(x, -1, z, BlocksAether.locked_angelic_stone);

//        this.setBlockWithOffset(x, -1, z, BlocksAether.treasure_chest);
//        ICoords chestCoords = new Coords(x + this.startX, -1 + this.startY, this.startZ);
//        Chest_Later.generate_later(this.worldObj, random, chestCoords, Rarity.RARE, BlocksAether.treasure_chest);

        return true;
    }

    public void generateGoldenOakSapling(int x, int y, int z) {
        this.setBlocks(LOCKED_ANGELIC_STONE, LOCKED_LIGHT_ANGELIC_STONE, 2);
        this.addPlaneY(x, y, z, 3, 3);

        this.setBlockWithOffset(x + 1, y, z + 1, BlocksAether.aether_dirt);
        this.setBlockWithOffset(x + 1, y + 1, z + 1, BlocksAether.golden_oak_sapling);

        for (int lineX = x; lineX < x + 3; lineX += 2) {
            for (int lineZ = z; lineZ < z + 3; lineZ += 2) {
                this.setBlockWithOffset(lineX, y + 1, lineZ, BlocksAether.ambrosium_torch);
            }
        }
    }

    public void generateChandelier(int x, int y, int z, int height) {
        for (int lineY = y + (height + 3); lineY < y + (height + 6); lineY++) {
            this.setBlockWithOffset(x, lineY, z, Material.OAK_FENCE);
        }

        for (int lineX = (x - 1); lineX < (x + 2); lineX++) {
            this.setBlockWithOffset(lineX, y + (height + 1), z, Material.GLOWSTONE);
        }

        for (int lineY = (y + height); lineY < y + (height + 3); lineY++) {
            this.setBlockWithOffset(x, lineY, z, Material.GLOWSTONE);
        }

        for (int lineZ = (z - 1); lineZ < (z + 2); lineZ++) {
            this.setBlockWithOffset(x, y + (height + 1), lineZ, Material.GLOWSTONE);
        }
    }

    public void generateColumn(int x, int y, int z, int yRange) {
        this.setBlocks(LOCKED_ANGELIC_STONE, LOCKED_LIGHT_ANGELIC_STONE, 20);
        this.addPlaneY(x, y, z, 3, 3);
        this.addPlaneY(x, y + yRange, z, 3, 3);
        this.setBlocks(BlocksAether.pillar, BlocksAether.pillar, 1);
        this.addLineY(x + 1, y, z + 1, yRange - 1);
        this.setBlockWithOffset(x + 1, y + (yRange - 1), z + 1, BlocksAether.pillar_top);
    }

    public void generateStaircase(int x, int y, int z, int height) {
        this.setBlocks(Material.AIR, Material.AIR, 1);
        this.addSolidBox(x + 1, y, z + 1, 4, height, 4);
        this.setBlocks(LOCKED_ANGELIC_STONE, LOCKED_LIGHT_ANGELIC_STONE, 5);
        this.addSolidBox(x + 2, y, z + 2, 2, height + 4, 2);

        Material slab = Material.SMOOTH_STONE_SLAB;

        Material double_slab = Material.SMOOTH_STONE_SLAB;

        this.setBlockWithOffset(x + 1, y, z + 1, slab);
        this.setBlockWithOffset(x + 2, y, z + 1, double_slab);
        this.setBlockWithOffset(x + 3, y + 1, z + 1, slab);
        this.setBlockWithOffset(x + 4, y + 1, z + 1, double_slab);
        this.setBlockWithOffset(x + 4, y + 2, z + 2, slab);
        this.setBlockWithOffset(x + 4, y + 2, z + 3, double_slab);
        this.setBlockWithOffset(x + 4, y + 3, z + 4, slab);
        this.setBlockWithOffset(x + 3, y + 3, z + 4, double_slab);
        this.setBlockWithOffset(x + 2, y + 4, z + 4, slab);
        this.setBlockWithOffset(x + 1, y + 4, z + 4, double_slab);

        if (height == 5) {
            return;
        }

        this.setBlockWithOffset(x + 1, y + 5, z + 3, slab);
        this.setBlockWithOffset(x + 1, y + 5, z + 2, double_slab);
        this.setBlockWithOffset(x + 1, y + 6, z + 1, slab);
        this.setBlockWithOffset(x + 2, y + 6, z + 1, double_slab);
        this.setBlockWithOffset(x + 3, y + 7, z + 1, slab);
        this.setBlockWithOffset(x + 4, y + 7, z + 1, double_slab);
        this.setBlockWithOffset(x + 4, y + 8, z + 2, slab);
        this.setBlockWithOffset(x + 4, y + 8, z + 3, double_slab);
        this.setBlockWithOffset(x + 4, y + 9, z + 4, slab);
        this.setBlockWithOffset(x + 3, y + 9, z + 4, double_slab);
    }

    public void generateDoorX(int x, int y, int z, int yF, int zF) {
        for (int yFinal = y; yFinal < y + yF; yFinal++) {
            for (int zFinal = z; zFinal < z + zF; zFinal++) {
                this.setBlockWithOffset(x, yFinal, zFinal, Material.AIR);
            }
        }
    }

    public void generateDoorZ(int z, int x, int y, int xF, int yF) {
        for (int xFinal = x; xFinal < x + xF; xFinal++) {
            for (int yFinal = y; yFinal < y + yF; yFinal++) {
                this.setBlockWithOffset(xFinal, yFinal, z, Material.AIR);
            }
        }
    }

//    //Get loot for normal chests scattered around
//    private ItemStack getNormalLoot(Random random) {
//        int item = random.nextInt(16);
//        switch (item) {
//            case 0:
//                return new ItemStack(ItemsAether.zanite_pickaxe);
//            case 1:
//                return new ItemStack(ItemsAether.skyroot_bucket);
//            case 2:
//                return new ItemStack(ItemsAether.dart_shooter);
//            case 3:
//                return ItemMoaEgg.getStackFromType(AetherMoaTypes.blue);
//            case 4:
//                return ItemMoaEgg.getStackFromType(AetherMoaTypes.white);
//            case 5:
//                return new ItemStack(ItemsAether.ambrosium_shard, random.nextInt(10) + 1);
//            case 6:
//                return new ItemStack(ItemsAether.dart, random.nextInt(5) + 1, 0);
//            case 7:
//                return new ItemStack(ItemsAether.dart, random.nextInt(3) + 1, 1);
//            case 8:
//                return new ItemStack(ItemsAether.dart, random.nextInt(3) + 1, 2);
//            case 9: {
//                if (random.nextInt(20) == 0)
//                    return new ItemStack(ItemsAether.aether_tune);
//                break;
//            }
//            case 10:
//                return new ItemStack(ItemsAether.skyroot_bucket, 1, 2);
//            case 11: {
//                if (random.nextInt(10) == 0)
//                    return new ItemStack(ItemsAether.ascending_dawn);
//                break;
//            }
//            case 12: {
//                if (random.nextInt(2) == 0)
//                    return new ItemStack(ItemsAether.zanite_boots);
//                if (random.nextInt(2) == 0)
//                    return new ItemStack(ItemsAether.zanite_helmet);
//                if (random.nextInt(2) == 0)
//                    return new ItemStack(ItemsAether.zanite_leggings);
//                if (random.nextInt(2) == 0)
//                    return new ItemStack(ItemsAether.zanite_chestplate);
//                break;
//            }
//            case 13: {
//                if (random.nextInt(4) == 0)
//                    return new ItemStack(ItemsAether.iron_pendant);
//            }
//            case 14: {
//                if (random.nextInt(10) == 0)
//                    return new ItemStack(ItemsAether.golden_pendant);
//            }
//            case 15: {
//                if (random.nextInt(15) == 0)
//                    return new ItemStack(ItemsAether.zanite_ring);
//            }
//        }
//
//        return new ItemStack(BlocksAether.ambrosium_torch, random.nextInt(4) + 1);
//    }
//
//    public static ItemStack getSilverLoot(Random random) {
//        int item = random.nextInt(13);
//
//        switch (item) {
//            case 0:
//                return new ItemStack(ItemsAether.gummy_swet, random.nextInt(15) + 1, random.nextInt(2));
//            case 1:
//                return new ItemStack(ItemsAether.lightning_sword);
//            case 2: {
//                if (random.nextBoolean())
//                    return new ItemStack(ItemsAether.valkyrie_axe);
//                if (random.nextBoolean())
//                    return new ItemStack(ItemsAether.valkyrie_shovel);
//                if (random.nextBoolean())
//                    return new ItemStack(ItemsAether.valkyrie_pickaxe);
//                break;
//            }
//            case 3:
//                return new ItemStack(ItemsAether.holy_sword);
//            case 4:
//                return new ItemStack(ItemsAether.valkyrie_helmet);
//            case 5:
//                return new ItemStack(ItemsAether.regeneration_stone);
//            case 6: {
//                if (random.nextBoolean())
//                    return new ItemStack(ItemsAether.neptune_helmet);
//                if (random.nextBoolean())
//                    return new ItemStack(ItemsAether.neptune_leggings);
//                if (random.nextBoolean())
//                    return new ItemStack(ItemsAether.neptune_chestplate);
//                break;
//            }
//            case 7: {
//                if (random.nextBoolean())
//                    return new ItemStack(ItemsAether.neptune_boots);
//                return new ItemStack(ItemsAether.neptune_gloves);
//            }
//            case 8:
//                return new ItemStack(ItemsAether.invisibility_cape);
//            case 9: {
//                if (random.nextBoolean())
//                    return new ItemStack(ItemsAether.valkyrie_boots);
//                return new ItemStack(ItemsAether.valkyrie_gloves);
//            }
//            case 10:
//                return new ItemStack(ItemsAether.valkyrie_leggings);
//            case 11:
//                if (random.nextBoolean())
//                    return new ItemStack(ItemsAether.valkyrie_chestplate);
//        }
//        return AetherConfig.valkyrieCapeEnabled() ? new ItemStack(ItemsAether.valkyrie_cape) : new ItemStack(ItemsAether.golden_feather);
//    }

}
