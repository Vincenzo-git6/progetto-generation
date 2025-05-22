export interface Pg
{
  id: number;
  name: string;
  description: string;
  hp : number;
  atk : number;
  def : number;
  spd : number;
  actionsName: string[];
  actionsDescriptions: string[];
  equipmentsName: string[];
  equipmentsDescriptions: string[];
  enumType: string;
  imageUrl?: string; //Optional
}
