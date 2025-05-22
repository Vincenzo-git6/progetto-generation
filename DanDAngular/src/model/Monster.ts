export interface Monster {
  id: number;
  name: string;
  description: string;
  danger: string;
  hp: number;
  atk: number;
  def: number;
  spd: number;
  actionsName?: string[];
  actionsDescriptions?: string[];
  imageUrl?: string;
}
