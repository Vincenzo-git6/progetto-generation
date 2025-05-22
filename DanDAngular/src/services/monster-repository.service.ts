import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Monster} from '../model/Monster';

@Injectable({
  providedIn: 'root'
})
export class MonsterRepositoryService
{
  constructor(private http: HttpClient) { }

  getAllMonsters()
  {
    return this.http.get<Monster[]>('/api/monsters/all');
  }

  getMonsterById(id: number)
  {
    return this.http.get<Monster[]>(`/api/monsters/${id}`);
  }
}
