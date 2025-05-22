import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Pg} from '../model/Pg';

@Injectable({
  providedIn: 'root'
})
export class PgRepositoryService {

  constructor(private http: HttpClient) { }

  getAllPgs()
  {
    return this.http.get<Pg[]>('/api/playables/all')
  }

  getPgById(id:number){
    return this.http.get<Pg[]>(`/api/playables/${id}`)
  }
}
